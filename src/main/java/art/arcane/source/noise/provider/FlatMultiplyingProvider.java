package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlatMultiplyingProvider implements NoisePlane {
    private final NoisePlane generator;
    private final double value;

    @Override
    public double noise(double x) {
        return generator.noise(x) * value;
    }

    @Override
    public double noise(double x, double y) {
        return generator.noise(x,y) *value;
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(x,y,z)  *value;
    }

    @Override
    public double getMaxOutput() {
        return generator.getMaxOutput() *value;
    }

    @Override
    public double getMinOutput() {
        return generator.getMinOutput() *value;
    }
}
