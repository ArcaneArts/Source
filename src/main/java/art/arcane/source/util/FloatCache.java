package art.arcane.source.util;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.NoiseTarget;
import lombok.Data;
import lombok.Getter;

@Data
public class FloatCache implements NoiseTarget {
    private final float[] cache;
    private final int width;
    private final int height;

    public FloatCache(int width, int height)
    {
        this.width = width;
        this.height = height;
        cache = new float[width * height];
    }

    public void set(int x, int y, float v)
    {
        cache[((y%height) * width) + (x % width)] = v;
    }

    public float get(int x, int y)
    {
        return cache[((y%height) * width) + (x % width)];
    }

    @Override
    public void collect(NoisePlane plane) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                set(x, y, (float) plane.noise(x, y));
            }
        }
    }
}
