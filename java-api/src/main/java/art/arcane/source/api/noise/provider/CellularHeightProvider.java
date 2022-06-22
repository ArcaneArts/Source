package art.arcane.source.api.noise.provider;

import art.arcane.source.api.util.Double2;
import art.arcane.source.api.util.Double3;
import art.arcane.source.api.util.FastNoiseDouble;

public class CellularHeightProvider extends SeededProvider {
    public CellularHeightProvider(long seed) {
        super(seed);
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
        double distance2 = 999999;
        for(long xi = xr - 1; xi <= xr + 1; xi++) {
            for(long yi = yr - 1; yi <= yr + 1; yi++) {
                Double2 vec = CELL_2D[(int) hash2D(seed, xi, yi) & 255];
                double vecX = xi - x + vec.x;
                double vecY = yi - y + vec.y;
                double newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);
                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                distance = Math.min(distance, newDistance);
            }
        }

        return distance2 - distance - 1;
    }

    @Override
    public double noise(double x, double y, double z) {
        long xr = round(x);
        long yr = round(y);
        long zr = round(z);
        double distance = 999999;
        double distance2 = 999999;
        for(long xi = xr - 1; xi <= xr + 1; xi++) {
            for(long yi = yr - 1; yi <= yr + 1; yi++) {
                for(long zi = zr - 1; zi <= zr + 1; zi++) {
                    Double3 vec = CELL_3D[(int) hash3D(seed, xi, yi, zi) & 255];
                    double vecX = xi - x + vec.x;
                    double vecY = yi - y + vec.y;
                    double vecZ = zi - z + vec.z;
                    double newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);
                    distance2 = Math.max(Math.min(distance2, newDistance), distance);
                    distance = Math.min(distance, newDistance);
                }
            }
        }

        return distance2 - distance - 1;
    }
}
