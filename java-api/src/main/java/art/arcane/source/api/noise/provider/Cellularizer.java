package art.arcane.source.api.noise.provider;

import art.arcane.source.api.accessor.ValueAccessor3D;
import art.arcane.source.api.util.Double2;
import art.arcane.source.api.util.Double3;

public class Cellularizer extends SeededProvider {
    private final ValueAccessor3D generator;

    public Cellularizer(ValueAccessor3D generator, long seed) {
        super(seed);
        this.generator = generator;
    }

    @Override
    public double noise(double x) {
        return noise(x, 0);
    }

    @Override
    public double noise(double x, double y) {
        long xr = round(x);
        long yr = round(y);

        double distance = 999999;
        long xc = 0, yc = 0, zc = 0;
        for(long xi = xr - 1; xi <= xr + 1; xi++) {
            for(long yi = yr - 1; yi <= yr + 1; yi++) {
                Double2 vec = CELL_2D[(int) hash2D(seed, xi, yi) & 255];
                double vecX = xi - x + vec.x;
                double vecY = yi - y + vec.y;
                double newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

                if(newDistance < distance) {
                    distance = newDistance;
                    xc = xi;
                    yc = yi;
                }
            }
        }

        return generator.noise(xc, yc);
    }

    @Override
    public double noise(double x, double y, double z) {
        long xr = round(x);
        long yr = round(y);
        long zr = round(z);
        double distance = 999999;
        long xc = 0, yc = 0, zc = 0;
        for(long xi = xr - 1; xi <= xr + 1; xi++) {
            for(long yi = yr - 1; yi <= yr + 1; yi++) {
                for(long zi = zr - 1; zi <= zr + 1; zi++) {
                    Double3 vec = CELL_3D[(int) hash3D(seed, xi, yi, zi) & 255];
                    double vecX = xi - x + vec.x;
                    double vecY = yi - y + vec.y;
                    double vecZ = zi - z + vec.z;
                    double newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);

                    if(newDistance < distance) {
                        distance = newDistance;
                        xc = xi;
                        yc = yi;
                        zc = zi;
                    }
                }
            }
        }

        return generator.noise(xc, yc, zc);
    }
}
