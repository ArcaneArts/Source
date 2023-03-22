package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import art.arcane.source.interpolator.Interpolator;
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

        if(generator.getMinOutput() == min && generator.getMaxOutput() == max) {
            return generator.noise(x);
        }
        if(generator.getMinOutput() == -1 && generator.getMaxOutput() == 1 && min == 0) {
           return ((generator.noise(x)+1) * 0.5)*max;
        }

        return Interpolator.rangeScale(min, max, generator.getMinOutput(), generator.getMaxOutput(), generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
        if(generator.getMinOutput() == min && generator.getMaxOutput() == max) {
            return generator.noise(x, y);
        }
        if(generator.getMinOutput() == -1 && generator.getMaxOutput() == 1 && min == 0) {
            return ((generator.noise(x,y)+1) * 0.5)*max;
        }

        return Interpolator.rangeScale(min, max, generator.getMinOutput(), generator.getMaxOutput(), generator.noise(x, y));
    }

    @Override
    public double noise(double x, double y, double z) {

        if(generator.getMinOutput() == min && generator.getMaxOutput() == max) {
            return generator.noise(x, y, z);
        }
        if(generator.getMinOutput() == -1 && generator.getMaxOutput() == 1 && min == 0) {
            return ((generator.noise(x,y,z)+1) * 0.5)*max;
        }
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
