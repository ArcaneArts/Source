package art.arcane.source.util;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.NoiseTarget;

public class MirroredFloatCache extends FloatCache implements NoiseTarget {
    public MirroredFloatCache(int width, int height) {
        super(width, height);
    }

    public int zigZag(int coord, int size) {
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

    @Override
    public void set(int x, int y, float v) {
        cache[(zigZag(y, height) * width) + zigZag(x, width)] = v;
    }

    @Override
    public float get(int x, int y) {
        return cache[(zigZag(y, height) * width) + zigZag(x, width)];
    }
}
