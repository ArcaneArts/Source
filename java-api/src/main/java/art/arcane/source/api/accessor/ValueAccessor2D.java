package art.arcane.source.api.accessor;

@FunctionalInterface
public interface ValueAccessor2D extends ValueAccessor1D {
    default double noise(double x)
    {
        return noise(x, 0);
    }

    double noise(double x, double y);
}
