package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class CompiledProvider extends SeededProvider {
    private final NoisePlane generator;

    public CompiledProvider(NoisePlane generator) {
        super(0);
        this.generator = generator;
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
        return generator.getMaxOutput();
    }

    @Override
    public double getMinOutput() {
        return generator.getMinOutput();
    }
}
