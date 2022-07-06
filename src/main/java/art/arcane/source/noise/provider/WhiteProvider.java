package art.arcane.source.noise.provider;

public class WhiteProvider extends SeededProvider {
    public WhiteProvider(long seed) {
        super(seed);
    }

    @Override
    public boolean isScalable()
    {
        return false;
    }

    @Override
    public double noise(double x) {
        long xi = double2Long(x);
        return valCoord1D(seed, xi);
    }

    @Override
    public double noise(double x, double y) {
        long xi = double2Long(x);
        long yi = double2Long(y);
        return valCoord2D(seed, xi, yi);
    }

    @Override
    public double noise(double x, double y, double z) {
        long xi = double2Long(x);
        long yi = double2Long(y);
        long zi = double2Long(z);
        return valCoord3D(seed, xi, yi, zi);
    }
}
