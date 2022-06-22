package art.arcane.source.api.noise.provider;

import art.arcane.source.api.accessor.ValueAccessor3D;
import art.arcane.source.api.util.MirroredFloatCache;

public class MirroredCacheProvider implements ValueAccessor3D {
    private MirroredFloatCache cache;

    public MirroredCacheProvider(ValueAccessor3D generator, int width, int height)
    {
        cache = new MirroredFloatCache(width, height);

        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                cache.set(i, j, (float) generator.noise(i, j));
            }
        }
    }

    @Override
    public double noise(double x) {
        return noise(x, 0);
    }

    @Override
    public double noise(double x, double y) {
        try
        {
            return cache.get((int)x, (int)y);

        }

        catch(Throwable e)
        {
            return 0;
        }
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x, y == 0 ? z : y);
    }
}
