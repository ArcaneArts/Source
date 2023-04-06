package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class Clipper extends SeededProvider {
    private final NoisePlane generator;
    private final double max;
    private final double min;

    public Clipper(NoisePlane generator, double min, double max) {
        super(0);
        this.generator = generator;
        this.max = max;
        this.min = min;
    }

    private double applyClip(double value) {
        return Math.min(max, Math.max(value, min));
    }

    @Override
    public double noise(double x) {
        return applyClip(generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
       return applyClip(generator.noise(x, y));
    }

    @Override
    public double noise(double x, double y, double z) {
        return applyClip(generator.noise(x, y, z));
    }

    @Override
    public double getMaxOutput() {
        return max;
    }

    @Override
    public double getMinOutput() {
        return min;
    }
}
