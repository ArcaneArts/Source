package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class Stretcher extends SeededProvider {
    private final NoisePlane generator;
    private final double newMin;
    private final double newMax;

    public Stretcher(NoisePlane generator, double newMin, double newMax) {
        super(0);
        this.generator = generator;
        this.newMin = newMin;
        this.newMax = newMax;
    }

    @Override
    public double noise(double x) {
        return generator.noise(x);
    }

    @Override
    public double noise(double x, double y) {
       return generator.noise(x, y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(x, y, z);
    }

    @Override
    public double getMaxOutput() {
        return newMax;
    }

    @Override
    public double getMinOutput() {
        return newMin;
    }
}
