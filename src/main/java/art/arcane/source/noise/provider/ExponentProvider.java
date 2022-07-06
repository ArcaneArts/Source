package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExponentProvider implements NoisePlane
{
    private final NoisePlane generator;
    private final double exponent;

    @Override
    public double noise(double x) {
        return Math.pow(generator.noise(x), exponent);
    }

    @Override
    public double noise(double x, double y) {
        return Math.pow(generator.noise(x, y), exponent);
    }

    @Override
    public double noise(double x, double y, double z) {
        return Math.pow(generator.noise(x, y, z), exponent);
    }

    @Override
    public double getMaxOutput() {
        return generator.getMaxOutput();
    }

    @Override
    public double getMinOutput() {
        return generator.getMinOutput();
    }
}
