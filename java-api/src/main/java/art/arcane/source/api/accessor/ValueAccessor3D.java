package art.arcane.source.api.accessor;

@FunctionalInterface
public interface ValueAccessor3D extends ValueAccessor2D {
    default double noise(double x)
    {
        return noise(x, 0, 0);
    }

    default double noise(double x, double y)
    {
        return noise(x, y, 0);
    }

    double noise(double x, double y, double z);
}
