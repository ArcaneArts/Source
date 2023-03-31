package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class Contrastor extends SeededProvider {
    private final NoisePlane generator;
    private final double amount;
    private final double range;
    private final double midpoint;
    private final double divRange;

    public Contrastor(NoisePlane generator, double amount) {
        super(0);
        this.generator = generator;
        this.amount = amount;
        this.range = (getMaxOutput() - getMinOutput());
        this.midpoint = (getMaxOutput() + getMinOutput()) / 2D;
        this.divRange = 1D / range;
    }

    private double applyContrast(double value) {
        double min = getMinOutput();
        double max = getMaxOutput();
        double valuePercent = (value - min) * divRange;
        double valueMidpoint = (valuePercent - 0.5D) * amount;

        return Math.min(max, Math.max(valueMidpoint + 0.5D, min));
    }

    @Override
    public double noise(double x) {
        return applyContrast(generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
       return applyContrast(generator.noise(x, y));
    }

    @Override
    public double noise(double x, double y, double z) {
        return applyContrast(generator.noise(x, y, z));
    }
}
