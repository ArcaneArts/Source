package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import art.arcane.source.Source;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompiledProvider extends SeededProvider {
    private final NoisePlane generator;
    private ImageProvider compiled;
    private final int width;
    private final int height;

    public CompiledProvider(NoisePlane generator, int width, int height) {
        super(0);
        this.generator = generator.fit(0, 1);
        this.width = width;
        this.height = height;
        Source.cacheLoader.register(this);
    }

    @Override
    public double noise(double x) {
        if(compiled != null)
        {
            return compiled.noise(x);
        }

        return generator.noise(x);
    }

    @Override
    public double noise(double x, double y) {

        if(compiled != null)
        {
            return compiled.noise(x, y);
        }

       return generator.noise(x, y);
    }

    @Override
    public double noise(double x, double y, double z) {
        if(compiled != null)
        {
            return compiled.noise(x, y, z);
        }

        return generator.noise(x, y, z);
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
