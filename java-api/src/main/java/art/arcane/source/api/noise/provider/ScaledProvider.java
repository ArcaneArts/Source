package art.arcane.source.api.noise.provider;

import art.arcane.source.api.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScaledProvider implements NoisePlane
{
    private final NoisePlane generator;
    private final double scale;

    @Override
    public double noise(double x) {
        return generator.noise(x * scale);
    }

    @Override
    public double noise(double x, double y) {
        return generator.noise(x * scale, y * scale);
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(x * scale, y * scale, z * scale);
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
