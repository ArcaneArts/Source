package art.arcane.source.api.noise;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.interpolator.Interpolator;

public class Generator implements NoisePlane {
    private final NoisePlane provider;
    public double scale;
    private final double minInput;
    private final double maxInput;
    public double minOutput;
    public double maxOutput;
    public double power;
    public NoisePlane warp;

    public Generator(NoisePlane provider)
    {
        this.minInput = provider.getMinOutput();
        this.maxInput = provider.getMaxOutput();
        this.minOutput = 0;
        this.maxOutput = 1;
        this.provider = provider;
        this.scale = 1;
        this.power = 1;
    }

    public double getMaxOutput()
    {
        return maxOutput;
    }

    public double getMinOutput()
    {
        return minOutput;
    }

    private double processDouble(double v)
    {
        double r = (minInput == minOutput && maxInput == maxOutput) ? v : Interpolator.rangeScale(minOutput, maxOutput, minInput, maxInput, v);

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
