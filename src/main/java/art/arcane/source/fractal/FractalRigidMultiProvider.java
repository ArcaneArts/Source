package art.arcane.source.fractal;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.provider.SeededProvider;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Accessors(chain = true,
    fluent = true)
public class FractalRigidMultiProvider extends SeededProvider {
    private final NoisePlane[] planes;
    private final int octaves;
    private final double lacunarity;
    private final double gain;

    public FractalRigidMultiProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain, double lacunarity)
    {
        super(baseSeed);
        this.planes = new NoisePlane[octaves];
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        long seed = baseSeed;
        planes[0] = generatorFactory.apply(seed);
        for(int i = 1; i < octaves; i++) {
            planes[i] = generatorFactory.apply(++seed);
        }
    }

    public FractalRigidMultiProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves, double gain)
    {
        this(generatorFactory, baseSeed, octaves, gain, 2D);
    }

    public FractalRigidMultiProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed, int octaves)
    {
        this(generatorFactory, baseSeed, octaves, 0.5);
    }

    public FractalRigidMultiProvider(Function<Long, NoisePlane> generatorFactory, long baseSeed)
    {
        this(generatorFactory, baseSeed, 3);
    }

    @Override
    public double noise(double x) {
        double sum = 1- Math.abs(planes[0].noise(x));
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            amp *= gain;
            sum -= (1- Math.abs(planes[i].noise(x))) * amp;
        }

        return sum;
    }

    @Override
    public double noise(double x, double y) {
        double sum = 1- Math.abs(planes[0].noise(x,y));
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
            sum -= (1- Math.abs(planes[i].noise(x, y))) * amp;
        }

        return sum;
    }

    @Override
    public double noise(double x, double y, double z) {
        double sum = 1- Math.abs(planes[0].noise(x,y,z));
        double amp = 1;

        for(int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
            sum -= (1- Math.abs(planes[i].noise(x, y, z))) * amp;
        }

        return sum;
    }
}
