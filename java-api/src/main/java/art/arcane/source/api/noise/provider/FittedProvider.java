package art.arcane.source.api.noise.provider;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.interpolator.Interpolator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FittedProvider implements NoisePlane
{
    private final NoisePlane generator;
    private final double min;
    private final double max;

    @Override
    public double noise(double x) {
        return Interpolator.rangeScale(min, max, generator.getMinOutput(), generator.getMaxOutput(), generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
        return Interpolator.rangeScale(min, max, generator.getMinOutput(), generator.getMaxOutput(), generator.noise(x, y));
    }

    @Override
    public double noise(double x, double y, double z) {
        return Interpolator.rangeScale(min, max, generator.getMinOutput(), generator.getMaxOutput(), generator.noise(x, y, z));
    }

    @Override
    public double getMaxOutput() {
        return max;
    }

    @Override
    public double getMinOutput() {
        return min;
    }
}
