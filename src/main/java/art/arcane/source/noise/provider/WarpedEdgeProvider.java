package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import art.arcane.source.interpolator.Interpolator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WarpedEdgeProvider implements NoisePlane {
    private final NoisePlane generator;
    private final NoisePlane warp;
    private final double multiplier;
    private final int width;
    private final int height;
    private final double percent;
    private final int halfWidth;
    private final int halfHeight;
    private final double d1HalfWidth;
    private final double d1HalfHeight;
    private final double distance;
    private final double d1Distance;

    public WarpedEdgeProvider(NoisePlane generator, NoisePlane warp, double multiplier, int width, int height, double percent)
    {
        this.generator = generator;
        this.multiplier = multiplier;
        this.warp = warp;
        this.width = width;
        this.height = height;
        this.percent = percent;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
        this.d1HalfWidth = 1D / halfWidth;
        this.d1HalfHeight = 1D / halfHeight;
        this.distance = Math.sqrt(width * width + height * height);
        this.d1Distance = 1D / distance;
    }


    @Override
    public double noise(double x) {
        double xDist = Math.abs(((int)Math.abs(x) % width) - halfWidth) * d1HalfWidth;

        if(percent < xDist) {
            double n = warp.noise(x) * multiplier * (xDist - percent);
            return generator.noise(
                    x + n);
        }

        return generator.noise(x);
    }

    @Override
    public double noise(double x, double y) {
        double xDist = Math.abs(((int)Math.abs(x) % width) - halfWidth) * d1HalfWidth;
        double yDist = Math.abs(((int)Math.abs(y) % height) - halfHeight) * d1HalfHeight;

        if(percent < xDist || percent < yDist) {
            double realPercent =  Math.max(xDist, yDist);

            if(realPercent > percent)
            {
                double n = warp.noise(x, y) * multiplier * (realPercent - percent);
                return generator.noise(
                        x + n,
                        y - n);
            }
        }

        return generator.noise(x,y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x,y);
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
