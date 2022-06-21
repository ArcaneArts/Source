package art.arcane.source.api.noise;

import art.arcane.source.api.accessor.ValueAccessor3D;
import art.arcane.source.api.noise.provider.NoiseProvider;

public class Generator implements ValueAccessor3D {
    private final ValueAccessor3D provider;
    public double scale;
    public double minOutput;
    public double maxOutput;
    public double power;
    public ValueAccessor3D warp;
    public double warpCoordinateSpread;

    public Generator(NoiseProvider provider)
    {
        this.minOutput = 0;
        this.maxOutput = 1;
        this.provider = provider;
        this.scale = 1;
        this.power = 1;
        this.warpCoordinateSpread = 100000;
    }

    private double processDouble(double v)
    {
        double r = minOutput + ((v + 1) * 0.5) * (maxOutput - minOutput);

        if(power != 1)
        {
            r = Math.pow(r, power);
        }

        return r;
    }

    public double noise(double x) {
        if(warp != null)
        {
            return processDouble(provider.noise(warp.noise(x) + x * scale));
        }

        return processDouble(provider.noise(x * scale));
    }

    public double noise(double x, double y) {
        if(warp != null)
        {
            return processDouble(provider.noise(warp.noise(x) + x * scale,
                    warp.noise(y + warpCoordinateSpread) + y * scale));
        }

        return processDouble(provider.noise(x * scale, y * scale));
    }

    public double noise(double x, double y, double z) {
        if(warp != null)
        {
            return processDouble(provider.noise(warp.noise(x) + x * scale,
                    warp.noise(y + warpCoordinateSpread) + y * scale,
                    warp.noise(z + (warpCoordinateSpread * 2)) + z * scale));
        }
       return processDouble(provider.noise(x * scale, y * scale, z * scale));
    }
}
