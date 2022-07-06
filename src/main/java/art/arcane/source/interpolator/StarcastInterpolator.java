package art.arcane.source.interpolator;

import art.arcane.source.NoisePlane;

import static art.arcane.source.util.generated.CompiledStarcast.getStarcast;

public class StarcastInterpolator extends Interpolator
{
    private final double checks;
    
    public StarcastInterpolator(NoisePlane input, double scale, double checks) {
        super(input, scale);
        this.checks = checks;
    }

    @Override
    public double noise(double x) {
        return noise(x,0);
    }

    @Override
    public double noise(double x, double y) {
        return getStarcast((float)x, (float)y, (float)scale, (float)checks, input);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x, y);
    }
}
