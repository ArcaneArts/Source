package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinProvider implements NoisePlane {
    private final NoisePlane generator;
    private final NoisePlane other;

    @Override
    public double noise(double x) {
        return Math.min(generator.noise(x), other.noise(x));
    }

    @Override
    public double noise(double x, double y) {
        return Math.min(generator.noise(x,y), other.noise(x,y));
    }

    @Override
    public double noise(double x, double y, double z) {
        return Math.min(generator.noise(x,y,z), other.noise(x,y,z));
    }

    @Override
    public double getMaxOutput() {
        return Math.min(generator.getMaxOutput(),other.getMaxOutput());
    }

    @Override
    public double getMinOutput() {
        return Math.min(generator.getMinOutput(),other.getMinOutput());
    }
}
