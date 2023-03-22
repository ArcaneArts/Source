package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvertedProvider implements NoisePlane {
    private final NoisePlane generator;

    private double invert(double value)
    {
        return (generator.getMaxOutput() - value) + generator.getMinOutput();
    }

    @Override
    public double noise(double x) {
        return invert(generator.noise(x));
    }

    @Override
    public double noise(double x, double y) {
        return invert(generator.noise(x,y ));
    }

    @Override
    public double noise(double x, double y, double z) {
        return invert(generator.noise(x ,y,z));
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
