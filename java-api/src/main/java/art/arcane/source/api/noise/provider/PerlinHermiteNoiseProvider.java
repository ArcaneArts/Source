package art.arcane.source.api.noise.provider;

import art.arcane.source.api.interpolator.Interpolator;

public class PerlinHermiteNoiseProvider extends SeededProvider {
    public PerlinHermiteNoiseProvider(long seed) {
        super(seed);
    }

    @Override
    public double noise(double x) {
        long x0 = floor(x);
        long x1 = x0 + 1;
        double xs;
        double ys = 0;
        double zs = 0;
        xs = longerpHermiteFunc(x - x0);
        double xd0 = x - x0;
        double xd1 = xd0 - 1;
        double xf00 = Interpolator.lerp(GradCoord1D(seed, x0, xd0), GradCoord1D(seed, x1, xd1), xs);
        double xf10 = Interpolator.lerp(GradCoord1D(seed, x0, xd0), GradCoord1D(seed, x1, xd1), xs);
        double xf01 = Interpolator.lerp(GradCoord1D(seed, x0, xd0), GradCoord1D(seed, x1, xd1), xs);
        double xf11 = Interpolator.lerp(GradCoord1D(seed, x0, xd0), GradCoord1D(seed, x1, xd1), xs);
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
        double xd0 = x - x0;
        double yd0 = y - y0;
        double xd1 = xd0 - 1;
        double yd1 = yd0 - 1;
        double xf00 = Interpolator.lerp(GradCoord2D(seed, x0, y0, xd0, yd0), GradCoord2D(seed, x1, y0, xd1, yd0), xs);
        double xf10 = Interpolator.lerp(GradCoord2D(seed, x0, y1, xd0, yd1), GradCoord2D(seed, x1, y1, xd1, yd1), xs);
        double xf01 = Interpolator.lerp(GradCoord2D(seed, x0, y0, xd0, yd0), GradCoord2D(seed, x1, y0, xd1, yd0), xs);
        double xf11 = Interpolator.lerp(GradCoord2D(seed, x0, y1, xd0, yd1), GradCoord2D(seed, x1, y1, xd1, yd1), xs);
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
        double xd0 = x - x0;
        double yd0 = y - y0;
        double zd0 = z - z0;
        double xd1 = xd0 - 1;
        double yd1 = yd0 - 1;
        double zd1 = zd0 - 1;
        double xf00 = Interpolator.lerp(GradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0), GradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
        double xf10 = Interpolator.lerp(GradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0), GradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
        double xf01 = Interpolator.lerp(GradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1), GradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
        double xf11 = Interpolator.lerp(GradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1), GradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);
        double yf0 = Interpolator.lerp(xf00, xf10, ys);
        double yf1 = Interpolator.lerp(xf01, xf11, ys);
        return Interpolator.lerp(yf0, yf1, zs);
    }
}
