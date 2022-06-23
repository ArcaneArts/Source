package art.arcane.source.api.noise.provider;

import art.arcane.source.api.util.Double2;
import art.arcane.source.api.util.Vector2d;
import art.arcane.source.api.util.Vector3d;

public class SimplexProvider extends SeededProvider {
    public SimplexProvider(long seed) {
        super(seed);
    }

    @Override
    public double noise(double x, double y) {

        double ix = floor(x + (x + y) * K1);
        double iy = floor(y + (x + y) * K1);;
        double ax = x - ix + (ix + iy) * K2;
        double ay = y - iy + (ix + iy) * K2;
        double m = ay > ax ? 1 : 0;
        double ox = m;
        double oy = 1D - m;
        double bx = ax - ox + K2;
        double by = ay - oy + K2;
        double cx = ax - 1.0 + 2.0 * K2;
        double cy = ay - 1.0 + 2.0 * K2;
        double hx = Math.max(0.5 - Vector2d.dot(ax, ay, ax, ay), 0D);
        double hy = Math.max(0.5 - Vector2d.dot(bx, by, bx, by), 0D);
        double hz = Math.max(0.5 - Vector2d.dot(cx, cy, cx, cy), 0D);
        double hashax = valCoord2D(seed, (long)ix, (long)iy);
        double hashay = valCoord2D(seed+1, (long)ix, (long)iy);
        double hashbx = valCoord2D(seed+2, (long)(ix + oy), (long)(iy + ox));
        double hashby = valCoord2D(seed+3, (long)(ix + oy), (long)(iy + ox));
        double hashcx = valCoord2D(seed+4, (long)ix+1, (long)iy+1);
        double hashcy = valCoord2D(seed+5, (long)-iy+1, (long)ix+1);
        double nx = hx * hx * hx * hx * Vector2d.dot(ax, ay, hashax, hashay);
        double ny = hy * hy * hy * hy * Vector2d.dot(bx, by, hashbx, hashby);
        double nz = hz * hz * hz * hz * Vector2d.dot(ax, ay, hashcx, hashcy);
        return Vector3d.dot(nx, ny, nz, 70, 70, 70);
    }

    @Override
    public double noise(double x, double y, double z) {
        double t = (x + y + z) * F3;
        long i = floor(x + t);
        long j = floor(y + t);
        long k = floor(z + t);
        t = (i + j + k) * G3;
        double x0 = x - (i - t);
        double y0 = y - (j - t);
        double z0 = z - (k - t);
        long i1, j1, k1;
        long i2, j2, k2;

        if(x0 >= y0) {
            if(y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if(x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else // x0 < z0
            {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else // x0 < y0
        {
            if(y0 < z0) {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else if(x0 < z0) {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else // x0 >= z0
            {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            }
        }

        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + F3;
        double y2 = y0 - j2 + F3;
        double z2 = z0 - k2 + F3;
        double x3 = x0 + G33;
        double y3 = y0 + G33;
        double z3 = z0 + G33;

        double n0, n1, n2, n3;

        t = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if(t < 0)
            n0 = 0;
        else {
            t *= t;
            n0 = t * t * gradCoord3D(seed, i, j, k, x0, y0, z0);
        }

        t = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if(t < 0)
            n1 = 0;
        else {
            t *= t;
            n1 = t * t * gradCoord3D(seed, i + i1, j + j1, k + k1, x1, y1, z1);
        }

        t = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if(t < 0)
            n2 = 0;
        else {
            t *= t;
            n2 = t * t * gradCoord3D(seed, i + i2, j + j2, k + k2, x2, y2, z2);
        }

        t = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if(t < 0)
            n3 = 0;
        else {
            t *= t;
            n3 = t * t * gradCoord3D(seed, i + 1, j + 1, k + 1, x3, y3, z3);
        }

        return 32 * (n0 + n1 + n2 + n3);
    }
}
