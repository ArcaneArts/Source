package art.arcane.source.api.fractal;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.noise.provider.SeededProvider;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Accessors(chain = true,
    fluent = true)
public class FractalFBMProvider extends SeededProvider {
    private final NoisePlane[] planes;
    private final int octaves;
    private final double lacunarity;
    private final double gain;
    private final double bounding;

    public FractalFBMProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain, double lacunarity)
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

    public FractalFBMProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain)
    {
        this(generatorFactory, baseSeed, octaves, gain, 2D);
    }

    public FractalFBMProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves)
    {
        this(generatorFactory, baseSeed, octaves, 0.5);
    }

    public FractalFBMProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed)
    {
        this(generatorFactory, baseSeed, 3);
    }

    @Override
    public double noise(double x) {
        double sum = planes[0].noise(x);
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            amp *= gain;
            sum += planes[i].noise(x) * amp;
        }

        return sum * bounding;
    }

    @Override
    public double noise(double x, double y) {
        double sum = planes[0].noise(x,y);
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
            sum += planes[i].noise(x, y) * amp;
        }

        return sum * bounding;
    }

    @Override
    public double noise(double x, double y, double z) {
        double sum = planes[0].noise(x,y,z);
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
            sum += planes[i].noise(x, y, z) * amp;
        }

        return sum * bounding;
    }
}
