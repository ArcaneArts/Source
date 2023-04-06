package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;

public class EdgeDetector extends SeededProvider {
    private final NoisePlane generator;
    private final double threshold;
    private final double diff;
    private final boolean fast;

    public EdgeDetector(NoisePlane generator, double threshold, boolean fast) {
        super(0);
        this.generator = generator;
        this.fast = fast;
        this.threshold = threshold;
        this.diff = 1D / (getMaxOutput() - getMinOutput());
    }

    @Override
    public double noise(double x) {
        double a = generator.noise(x);
        double b = generator.noise(x+1);
        double c = generator.noise(x-1);
        if(Math.abs(a - b) * diff > threshold || Math.abs(a - c) * diff > threshold) {
            return getMaxOutput();
        }

        return getMinOutput();
    }

    @Override
    public double noise(double x, double y) {
        double c = generator.noise(x, y);

        if(Math.abs(c - generator.noise(x+1, y)) * diff > threshold) {
            return getMaxOutput();
        }
        if(Math.abs(c - generator.noise(x, y+1)) * diff > threshold) {
            return getMaxOutput();
        }
        if(!fast) {
            if(Math.abs(c - generator.noise(x-1, y)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x+1, y+1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x-1, y+1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x+1, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x-1, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
        }


        return getMinOutput();
    }

    @Override
    public double noise(double x, double y, double z) {
        double c = generator.noise(x, y);

        if(Math.abs(c - generator.noise(x+1, y)) * diff > threshold) {
            return getMaxOutput();
        }
        if(Math.abs(c - generator.noise(x, y+1)) * diff > threshold) {
            return getMaxOutput();
        }
        if(!fast) {
            if(Math.abs(c - generator.noise(x-1, y)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x+1, y+1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x-1, y+1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x+1, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
            if(Math.abs(c - generator.noise(x-1, y-1)) * diff > threshold) {
                return getMaxOutput();
            }
        }


        return getMinOutput();
    }

    @Override
    public boolean supports3D() {
        return false;
    }
}
