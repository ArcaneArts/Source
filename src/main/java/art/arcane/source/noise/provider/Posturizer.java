package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import art.arcane.source.interpolator.Interpolator;

public class Posturizer extends SeededProvider {
    private final NoisePlane generator;
    private final int values;
    private final double range;
    private final double chunk;
    private final double divChunk;

    public Posturizer(NoisePlane generator, int values) {
        super(0);
        this.generator = generator;
        this.values = values;
        this.range = (getMaxOutput() - getMinOutput());
        this.chunk = range / values;
        this.divChunk = 1D / chunk;
    }

    private double applyPosturize(double v) {
        double a = Interpolator.rangeScale(0, values, generator.getMinOutput(), generator.getMaxOutput(), v);
        return Math.round(a);
    }

    @Override
    public double noise(double x) {
        return applyPosturize(generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
       return applyPosturize(generator.noise(x, y));
    }

    @Override
    public double noise(double x, double y, double z) {
        return applyPosturize(generator.noise(x, y, z));
    }

    @Override
    public double getMaxOutput() {
        return values;
    }

    @Override
    public double getMinOutput() {
        return 0;
    }
}
