package art.arcane.source.util;

import art.arcane.source.NoisePlane;
import art.arcane.source.fractal.FractalBillowProvider;
import art.arcane.source.fractal.FractalFBMProvider;
import art.arcane.source.noise.provider.*;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public enum NoisePreset
{
    // RAW Noise Providers
    SIMPLEX(SimplexProvider::new),
    CELLULAR(CellularProvider::new),
    CELLULAR_EDGE((s) -> NoisePreset.CELLULAR.create(s).edgeDetect(0.000000001, true)),
    CELLULAR_EDGE_THIN((s) -> NoisePreset.CELLULAR.create(s).scale(0.25).edgeDetect(0.000000001, true)),
    CELLULAR_EDGE_HAIRTHIN((s) -> NoisePreset.CELLULAR.create(s).scale(0.1).edgeDetect(0.000000001, true)),
    CELLULAR_EDGE_THICK((s) -> NoisePreset.CELLULAR.create(s).scale(2).edgeDetect(0.000000001, true)),
    CELLULAR_HEIGHT(CellularHeightProvider::new),
    CLOVER(CloverProvider::new),
    FLAT(FlatProvider::new),
    PERLIN(PerlinProvider::new),
    WHITE(WhiteProvider::new),
    VALUE(ValueLinearProvider::new),
    VALUE_HERMITE(ValueHermiteProvider::new),
    BENDPLEX((s) -> new SimplexProvider(s).warp(new PerlinProvider(s+1), 3.5, 0.25)),
    DROOPY((s) -> new SimplexProvider(s).warp(new PerlinProvider(s+1), 1.7, 0.75)),
    WRINKLEPLEX((s) -> new SimplexProvider(s).warp(new PerlinProvider(s+1), 5.7, 0.15)),
        NATURAL((s) -> new SimplexProvider(s).octave(2, 0.5)
        .warp(new PerlinProvider(s+2), 0.99, 0.55)
        .warp(new PerlinProvider(s+1), 8.7, 0.07)),
    WETLAND((s) -> new FractalBillowProvider(PerlinProvider::new, s, 1, 0, 2D)),
    LAVA((s) -> new FractalBillowProvider(PerlinProvider::new, s, 3, 0.5, 2D)),
    SPATTER((s) -> new FractalFBMProvider(SimplexProvider::new, s, 4, 0.6, 2.75D)),
    THERMA((s) -> new SimplexProvider(s).octave(3, 0.5)
        .warp(LAVA.create(s + 1), 0.75, 3).scale(4)),
    THERMA_EDGE((s) -> NoisePreset.THERMA.create(s).scale(0.1).exponent(10)
            .edgeDetect(0.000002, true)),
    LAVA_EDGE((s) -> NoisePreset.LAVA.create(s).scale(0.1).exponent(75)
            .edgeDetect(0.000002, true)),


    ;

    private final Function<Long, NoisePlane> plane;

    NoisePreset(Function<Long, NoisePlane> plane) {
        this.plane = plane;
    }

    public NoisePlane create(long seed) {
        return plane.apply(seed).scale(0.1);
    }

    public static BenchScore benchmark(long maxTime) {
        double lint = 0;
        long seed = (long) (Math.random() * Long.MAX_VALUE);
        long divTimes = maxTime / NoisePreset.values().length;
        BenchScore s = new BenchScore();
        for(NoisePreset i : NoisePreset.values()) {
            PrecisionStopwatch p = PrecisionStopwatch.start();
            long s1 = 0;
            long s2 = 0;
            long s3 = 0;
            NoisePlane px = i.create(1337);
            long dt = divTimes / 3;

            while(p.getMilliseconds() < dt) {
                s1++;
                lint += px.noise(s1 ^ seed);
            }

            p.reset();
            p.begin();
            s.score1.put(i.name(), s1);

            while(p.getMilliseconds() < dt) {
                s2++;
                lint += px.noise(s2 * seed, s2 ^ seed);
            }

            p.reset();
            p.begin();
            s.score2.put(i.name(), s2);

            while(p.getMilliseconds() < dt) {
                s3++;
                lint += px.noise((s2 * seed) ^ seed, s2 * seed, s2 ^ seed);
            }

            s.score3.put(i.name(), s3);
        }

        return s;
    }

    @Data
    public static class BenchScore {
        Map<String, Long> score1 = new HashMap<>();
        Map<String, Long> score2 = new HashMap<>();
        Map<String, Long> score3 = new HashMap<>();

        public NoisePreset fastest1D(NoisePreset... presets)
        {
            NoisePreset f = presets[0];
            for(NoisePreset i : presets) {
                if(score1.get(i.name()) < score1.get(f.name())) {
                    f = i;
                }
            }

            return f;
        }

        public NoisePreset fastest2D(NoisePreset... presets)
        {
            NoisePreset f = presets[0];
            for(NoisePreset i : presets) {
                if(score2.get(i.name()) < score2.get(f.name())) {
                    f = i;
                }
            }

            return f;
        }

        public NoisePreset fastest3D(NoisePreset... presets)
        {
            NoisePreset f = presets[0];
            for(NoisePreset i : presets) {
                if(score3.get(i.name()) < score3.get(f.name())) {
                    f = i;
                }
            }

            return f;
        }

        public double percentFaster1D(NoisePreset a, NoisePreset b) {
            return (double) score1.get(a.name()) / (double) score1.get(b.name());
        }

        public double percentFaster2D(NoisePreset a, NoisePreset b) {
            return (double) score2.get(a.name()) / (double) score2.get(b.name());
        }

        public double percentFaster3D(NoisePreset a, NoisePreset b) {
            return (double) score3.get(a.name()) / (double) score3.get(b.name());
        }

        public List<NoisePreset> top1D() {
            return top1D(score1.size());
        }

        public List<NoisePreset> top2D() {
            return top2D(score2.size());
        }

        public List<NoisePreset> top3D() {
            return top3D(score3.size());
        }

        public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c2, c1) -> c1.getValue().compareTo(c2.getValue());
        }

        public void compare1D(NoisePreset a, NoisePreset b) {
            double pf = percentFaster1D(a, b);

            if(pf < 0.99)
            {
                pf = percentFaster1D(b, a);
                System.out.println(b.name() + " is " + (int)((pf-1)*100) + "% faster than " + a.name() + " (1D)");
            }

            else if(pf > 1.01)
            {
                System.out.println(a.name() + " is " + (int)((pf-1)*100) + "% faster than " + b.name() + " (1D)");
            }

            else {
                System.out.println(a.name() + " is the same speed as " + b.name() + " (1D)");
            }
        }

        public void compare2D(NoisePreset a, NoisePreset b) {
            double pf = percentFaster2D(a, b);

            if(pf < 0.99)
            {
                pf = percentFaster2D(b, a);
                System.out.println(b.name() + " is " + (int)((pf-1)*100) + "% faster than " + a.name() + " (2D)");
            }

            else if(pf > 1.01)
            {
                System.out.println(a.name() + " is " + (int)((pf-1)*100) + "% faster than " + b.name() + " (2D)");
            }

            else {
                System.out.println( a.name() + " is the same speed as " + b.name() + " (2D)");
            }
        }

        public void compare3D(NoisePreset a, NoisePreset b) {
            double pf = percentFaster3D(a, b);

            if(pf < 0.99)
            {
                pf = percentFaster3D(b, a);
                System.out.println(b.name() + " is " + (int)((pf-1)*100) + "% faster than " + a.name() + " (3D)");
            }

            else if(pf > 1.01)
            {
                System.out.println(a.name() + " is " + (int)((pf-1)*100) + "% faster than " + b.name() + " (3D)");
            }

            else {
                System.out.println(a.name() + " is the same speed as " + b.name() + " (3D)");
            }
        }

        public List<NoisePreset> top1D(int max) {
            return score1.entrySet().stream().sorted(comparingByValue())
                    .map(x -> NoisePreset.valueOf(x.getKey()))
                    .limit(max)
                    .toList();
        }

        public List<NoisePreset> top2D(int max) {
            return score2.entrySet().stream().sorted(comparingByValue())
                    .map(x -> NoisePreset.valueOf(x.getKey()))
                    .limit(max)
                    .toList();
        }

        public List<NoisePreset> top3D(int max) {
            return score3.entrySet().stream().sorted(comparingByValue())
                    .map(x -> NoisePreset.valueOf(x.getKey()))
                    .limit(max)
                    .toList();
        }

        public long getScore1D(NoisePreset p) {
            return score1.get(p.name());
        }

        public long getScore2D(NoisePreset p) {
            return score2.get(p.name());
        }

        public long getScore3D(NoisePreset p) {
            return score3.get(p.name());
        }
    }
}
