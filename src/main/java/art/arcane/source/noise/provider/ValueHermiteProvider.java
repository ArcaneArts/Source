package art.arcane.source.noise.provider;

import art.arcane.source.interpolator.Interpolator;

public class ValueHermiteProvider extends SeededProvider {
    public ValueHermiteProvider(long seed) {
        super(seed);
    }

    @Override
    public double noise(double x) {
        long x0 = floor(x);
        long x1 = x0 + 1;
        double xs = 0, ys = 0, zs = 0;
        xs = longerpHermiteFunc(x - x0);
        double xf00 = Interpolator.lerp(valCoord1D(seed, x0), valCoord1D(seed, x1), xs);
        double xf10 = Interpolator.lerp(valCoord1D(seed, x0), valCoord1D(seed, x1), xs);
        double xf01 = Interpolator.lerp(valCoord1D(seed, x0), valCoord1D(seed, x1), xs);
        double xf11 = Interpolator.lerp(valCoord1D(seed, x0), valCoord1D(seed, x1), xs);
        double yf0 = Interpolator.lerp(xf00, xf10, ys);
        double yf1 = Interpolator.lerp(xf01, xf11, ys);
        return Interpolator.lerp(yf0, yf1, zs);
    }

    @Override
    public double noise(double x, double y) {
        long x0 = floor(x);
        long y0 = floor(y);
        long x1 = x0 + 1;
        long y1 = y0 + 1;
        double xs = 0, ys = 0, zs = 0;
        xs = longerpHermiteFunc(x - x0);
        ys = longerpHermiteFunc(y - y0);
        double xf00 = Interpolator.lerp(valCoord2D(seed, x0, y0), valCoord2D(seed, x1, y0), xs);
        double xf10 = Interpolator.lerp(valCoord2D(seed, x0, y1), valCoord2D(seed, x1, y1), xs);
        double xf01 = Interpolator.lerp(valCoord2D(seed, x0, y0), valCoord2D(seed, x1, y0), xs);
        double xf11 = Interpolator.lerp(valCoord2D(seed, x0, y1), valCoord2D(seed, x1, y1), xs);
        double yf0 = Interpolator.lerp(xf00, xf10, ys);
        double yf1 = Interpolator.lerp(xf01, xf11, ys);
        return Interpolator.lerp(yf0, yf1, zs);
    }

    @Override
    public double noise(double x, double y, double z) {
        long x0 = floor(x);
        long y0 = floor(y);
        long z0 = floor(z);
        long x1 = x0 + 1;
        long y1 = y0 + 1;
        long z1 = z0 + 1;
        double xs = 0, ys = 0, zs = 0;
        xs = longerpHermiteFunc(x - x0);
        ys = longerpHermiteFunc(y - y0);
        zs = longerpHermiteFunc(z - z0);
        double xf00 = Interpolator.lerp(valCoord3D(seed, x0, y0, z0), valCoord3D(seed, x1, y0, z0), xs);
        double xf10 = Interpolator.lerp(valCoord3D(seed, x0, y1, z0), valCoord3D(seed, x1, y1, z0), xs);
        double xf01 = Interpolator.lerp(valCoord3D(seed, x0, y0, z1), valCoord3D(seed, x1, y0, z1), xs);
        double xf11 = Interpolator.lerp(valCoord3D(seed, x0, y1, z1), valCoord3D(seed, x1, y1, z1), xs);
        double yf0 = Interpolator.lerp(xf00, xf10, ys);
        double yf1 = Interpolator.lerp(xf01, xf11, ys);
        return Interpolator.lerp(yf0, yf1, zs);
    }
}
