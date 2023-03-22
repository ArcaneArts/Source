package art.arcane.source.util;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.NoiseTarget;

public class MirroredFloatCache implements NoiseTarget {
    private final float[] cache;
    private final int width;
    private final int height;

    public MirroredFloatCache(int width, int height)
    {
        this.width = width;
        this.height = height;
        cache = new float[width * height];
    }

    public int zigZag(int coord, int size)
    {
        if(coord < 0)
        {
            coord = Math.abs(coord);
        }

        if(coord % (size * 2) >= size)
        {
            return (size) - (coord % size)-1;
        }

        else {
            return coord % size;
        }
    }

    public void set(int x, int y, float v)
    {
        cache[(zigZag(y, height) * width) + zigZag(x, width)] = v;
    }

    public float get(int x, int y)
    {
        return cache[(zigZag(y, height) * width) + zigZag(x, width)];
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
