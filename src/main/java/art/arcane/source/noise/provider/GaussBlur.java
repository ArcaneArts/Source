package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import art.arcane.source.interpolator.Interpolator;

public class GaussBlur extends SeededProvider {
    private final NoisePlane generator;
    private final double sigma;

    public GaussBlur(NoisePlane generator, double sigma) {
        super(0);
        this.generator = generator;
        this.sigma = sigma;
    }

    @Override
    public double noise(double x) {
        return generator.noise(x);
    }

    @Override
    public double noise(double x, double y) {
        double sum = 0;
        double o = 0;
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                double weight = Interpolator.bigauss(i, j, sigma);
                o+= generator.noise(x+i,y+j) * weight;
                sum += weight;
            }
        }

        return o / sum;
    }

    @Override
    public double noise(double x, double y, double z) {
        return generator.noise(x,y,z);
    }
}
