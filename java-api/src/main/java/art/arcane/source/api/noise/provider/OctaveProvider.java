package art.arcane.source.api.noise.provider;

import art.arcane.source.api.NoisePlane;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OctaveProvider implements NoisePlane
{
    private final NoisePlane generator;
    private final int octaves;
    private final double gain;
    private final double multiplier;

    public OctaveProvider(NoisePlane generator, int octaves, double gain)
    {
        this.generator = generator;
        this.octaves = octaves;
        this.gain = gain;
        this.multiplier = 1D / (double)octaves;
    }

    @Override
    public double noise(double x) {
        double n = generator.noise(x);

        for(int i = 1; i < octaves; i++)
        {
            n += generator.noise((x + (i * 100000)) * gain * i);
        }

        return n * multiplier;
    }

    @Override
    public double noise(double x, double y) {
        double n = generator.noise(x, y);

        for(int i = 1; i < octaves; i++)
        {
            n += generator.noise((x + (i * 100000)) * gain * i, (y + (i * 100000)) * gain * i);
        }

        return n * multiplier;
    }

    @Override
    public double noise(double x, double y, double z) {
        double n = generator.noise(x);

        for(int i = 1; i < octaves; i++)
        {
            n += generator.noise((x + (i * 100000)) * gain * i, (y + (i * 100000)) * gain * i, (z + (i * 100000)) * gain * i);
        }

        return n * multiplier;
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
