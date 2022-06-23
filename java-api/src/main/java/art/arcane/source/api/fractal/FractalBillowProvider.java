package art.arcane.source.api.fractal;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.noise.Generator;
import art.arcane.source.api.noise.provider.SeededProvider;

import java.util.function.Function;

public class FractalBillowProvider extends SeededProvider {
    private final NoisePlane[] planes;
    public final int octaves;
    public final double lacunarity;
    public final double gain;
    private final double bounding;

    public FractalBillowProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain, double lacunarity)
    {
        super(baseSeed);
        this.planes = new NoisePlane[octaves];
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        double amp = gain;
        double ampFractal = 1;
        long seed = baseSeed;
        planes[0] = generatorFactory.apply(seed);
        for(int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
            planes[i] = generatorFactory.apply(++seed);
        }
        bounding = 1 / ampFractal;
    }

    public FractalBillowProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain)
    {
        this(generatorFactory, baseSeed, octaves, gain, 2D);
    }

    public FractalBillowProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves)
    {
        this(generatorFactory, baseSeed, octaves, 0.5);
    }

    public FractalBillowProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed)
    {
        this(generatorFactory, baseSeed, 3);
    }

    private NoisePlane shape(NoisePlane plane)
    {
        if(plane.getMinOutput() != -1 || plane.getMaxOutput() != 1)
        {
            Generator g = new Generator(plane);
            g.minOutput = -1;
            g.maxOutput = 1;

            return g;
        }

        return plane;
    }

    @Override
    public double noise(double x) {
        double sum = (Math.abs(planes[0].noise(x)) * 2) - 1;
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            amp *= gain;
            sum += ((Math.abs(planes[i].noise(x)) * 2) - 1) * amp;
        }

        return sum * bounding;
    }

    @Override
    public double noise(double x, double y) {
        double sum = (Math.abs(planes[0].noise(x,y)) * 2) - 1;
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
            sum += ((Math.abs(planes[i].noise(x, y)) * 2) - 1) * amp;
        }

        return sum * bounding;
    }

    @Override
    public double noise(double x, double y, double z) {
        double sum = (Math.abs(planes[0].noise(x,y,z)) * 2) - 1;
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
            sum += ((Math.abs(planes[i].noise(x, y, z)) * 2) - 1) * amp;
        }

        return sum * bounding;
    }
}
