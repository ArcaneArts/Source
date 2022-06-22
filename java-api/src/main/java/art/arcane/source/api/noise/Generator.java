package art.arcane.source.api.noise;

import art.arcane.source.api.accessor.ValueAccessor3D;
import art.arcane.source.api.interpolator.Interpolator;
import art.arcane.source.api.noise.provider.NoiseProvider;

public class Generator implements ValueAccessor3D {
    private final ValueAccessor3D provider;
    public double scale;
    public double minInput;
    public double maxInput;
    public double minOutput;
    public double maxOutput;
    public double power;
    public ValueAccessor3D warp;

    public Generator(ValueAccessor3D provider)
    {
        this.minInput = -1;
        this.maxInput = 1;
        this.minOutput = 0;
        this.maxOutput = 1;
        this.provider = provider;
        this.scale = 1;
        this.power = 1;
    }

    private double processDouble(double v)
    {
        double r = Interpolator.rangeScale(minOutput, maxOutput, minInput, maxInput, v);

        if(power != 1)
        {
            r = Math.pow(r, power);
        }

        return r;
    }

    public double noise(double x) {
        return processDouble(provider.noise(
            warp != null ? warp.noise(x) + x * scale : x * scale
        ));
    }

    public double noise(double x, double y) {
        return processDouble(provider.noise(
            warp != null ? warp.noise(x, y) + x * scale : x * scale,
            warp != null ? warp.noise(y, x) + y * scale : y * scale
        ));
    }

    public double noise(double x, double y, double z) {
        return processDouble(provider.noise(
            warp != null ? warp.noise(x, y, z) + x * scale : x * scale,
            warp != null ? warp.noise(y, z, x) + y * scale : y * scale,
            warp != null ? warp.noise(z, x, y) + z * scale : z * scale
        ));
    }
}
