package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class Sloper extends SeededProvider {
    private final NoisePlane generator;
    private final double radius;

    public Sloper(NoisePlane generator, double radius) {
        super(0);
        this.generator = generator;
        this.radius = radius;
    }

    @Override
    public double noise(double x) {
        return getMinOutput(); // TODO
    }

    @Override
    public double noise(double x, double y) {
        double height = generator.noise(x, y);
        double dx = generator.noise(x + radius, y) - height;
        double dy = generator.noise(x, y + radius) - height;

        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public double noise(double x, double y, double z) {
        double height = generator.noise(x, y, z);
        double dx = generator.noise(x + radius, y, z) - height;
        double dy = generator.noise(x, y + radius,z) - height;
        double dz = generator.noise(x, y, z + radius) - height;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double getMaxOutput()
    {
        return generator.getMaxOutput() - generator.getMinOutput();
    }

    @Override
    public double getMinOutput()
    {
        return -getMaxOutput();
    }
}
