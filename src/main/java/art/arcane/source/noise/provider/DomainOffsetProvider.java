package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainOffsetProvider implements NoisePlane {
    private final NoisePlane generator;
    private final double x;
    private final double y;
    private final double z;

    @Override
    public double noise(double x) {
        return generator.noise(x +this.x);
    }

    @Override
    public double noise(double x, double y) {
        return generator.noise(x +this.x, y + this.y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(x +this.x, y +this.y, z +this.z);
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
