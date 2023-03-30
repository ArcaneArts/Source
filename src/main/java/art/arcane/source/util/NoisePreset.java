package art.arcane.source.util;

import art.arcane.source.NoisePlane;
import art.arcane.source.fractal.FractalBillowProvider;
import art.arcane.source.fractal.FractalFBMProvider;
import art.arcane.source.noise.provider.CellularHeightProvider;
import art.arcane.source.noise.provider.CellularProvider;
import art.arcane.source.noise.provider.CloverProvider;
import art.arcane.source.noise.provider.FlatProvider;
import art.arcane.source.noise.provider.PerlinProvider;
import art.arcane.source.noise.provider.SimplexProvider;
import art.arcane.source.noise.provider.WhiteProvider;

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
}
