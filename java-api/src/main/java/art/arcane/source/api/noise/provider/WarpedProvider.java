package art.arcane.source.api.noise.provider;

import art.arcane.source.api.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WarpedProvider implements NoisePlane
{
    private final NoisePlane generator;
    private final NoisePlane warp;
    private final double scale;
    private final double multiplier;

    @Override
    public double noise(double x) {
        return generator.noise((warp.noise(x * scale) * multiplier) + x);
    }

    @Override
    public double noise(double x, double y) {
        return generator.noise(
            (warp.noise(x * scale, y * scale) * multiplier) + x,
            (warp.noise(y * scale, -x * scale) * multiplier) + y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(
            (warp.noise(-x * scale, y * scale, z * scale) * multiplier) + x,
            (warp.noise(y * scale, -z * scale, x * scale) * multiplier) + y,
            (warp.noise(z * scale, x * scale, -y * scale) * multiplier) + z);
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
