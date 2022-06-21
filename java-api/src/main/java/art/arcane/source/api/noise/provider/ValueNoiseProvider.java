package art.arcane.source.api.noise.provider;

public class ValueNoiseProvider extends SeededNoiseProvider {
    public ValueNoiseProvider(long seed) {
        super(seed);
    }

    @Override
    public double noise(double x) {
        return 1;
    }

    @Override
    public double noise(double x, double y) {
        return 1;
    }

    @Override
    public double noise(double x, double y, double z) {
        return 1;
    }
}
