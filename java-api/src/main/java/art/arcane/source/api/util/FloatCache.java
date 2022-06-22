package art.arcane.source.api.util;

public class FloatCache {
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
}
