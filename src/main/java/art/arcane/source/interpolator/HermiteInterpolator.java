package art.arcane.source.interpolator;

import art.arcane.source.NoisePlane;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true,
    fluent = true)
public class HermiteInterpolator extends Interpolator
{
    @Getter @Setter
    private double tension;
    @Getter @Setter
    private double bias;

    public HermiteInterpolator(NoisePlane input, double scale) {
        super(input, scale);
        this.tension = 0;
        this.bias = 0;
    }

    @Override
    public double noise(double x) {
        int[] box = getScaleBoundsC1D4(x);
        return hermite(
                input.noise(box[X1_D4]),
                input.noise(box[X2_D4]),
                input.noise(box[X3_D4]),
                input.noise(box[X4_D4]),
                normalize(box[X2_D4], box[X3_D4], x),
                tension, bias);
    }

    @Override
    public double noise(double x, double y) {
        int[] box = getScaleBoundsC2D4(x, y);
        return bihermite(
                input.noise(box[X1_D4], box[Y1_D4]),
                input.noise(box[X1_D4], box[Y2_D4]),
                input.noise(box[X1_D4], box[Y3_D4]),
                input.noise(box[X1_D4], box[Y4_D4]),
                input.noise(box[X2_D4], box[Y1_D4]),
                input.noise(box[X2_D4], box[Y2_D4]),
                input.noise(box[X2_D4], box[Y3_D4]),
                input.noise(box[X2_D4], box[Y4_D4]),
                input.noise(box[X3_D4], box[Y1_D4]),
                input.noise(box[X3_D4], box[Y2_D4]),
                input.noise(box[X3_D4], box[Y3_D4]),
                input.noise(box[X3_D4], box[Y4_D4]),
                input.noise(box[X4_D4], box[Y1_D4]),
                input.noise(box[X4_D4], box[Y2_D4]),
                input.noise(box[X4_D4], box[Y3_D4]),
                input.noise(box[X4_D4], box[Y4_D4]),
                normalize(box[X2_D4], box[X3_D4], x),
                normalize(box[Y2_D4], box[Y3_D4], y),
                tension, bias);
    }

    @Override
    public double noise(double x, double y, double z) {
        int[] box = getScaleBoundsC3D4(x, y, z);
        return trihermite(
                input.noise(box[X1_D4], box[Y1_D4], box[Z1_D4]),
                input.noise(box[X1_D4], box[Y2_D4], box[Z1_D4]),
                input.noise(box[X1_D4], box[Y3_D4], box[Z1_D4]),
                input.noise(box[X1_D4], box[Y4_D4], box[Z1_D4]),
                input.noise(box[X2_D4], box[Y1_D4], box[Z1_D4]),
                input.noise(box[X2_D4], box[Y2_D4], box[Z1_D4]),
                input.noise(box[X2_D4], box[Y3_D4], box[Z1_D4]),
                input.noise(box[X2_D4], box[Y4_D4], box[Z1_D4]),
                input.noise(box[X3_D4], box[Y1_D4], box[Z1_D4]),
                input.noise(box[X3_D4], box[Y2_D4], box[Z1_D4]),
                input.noise(box[X3_D4], box[Y3_D4], box[Z1_D4]),
                input.noise(box[X3_D4], box[Y4_D4], box[Z1_D4]),
                input.noise(box[X4_D4], box[Y1_D4], box[Z1_D4]),
                input.noise(box[X4_D4], box[Y2_D4], box[Z1_D4]),
                input.noise(box[X4_D4], box[Y3_D4], box[Z1_D4]),
                input.noise(box[X4_D4], box[Y4_D4], box[Z1_D4]),
                input.noise(box[X1_D4], box[Y1_D4], box[Z2_D4]),
                input.noise(box[X1_D4], box[Y2_D4], box[Z2_D4]),
                input.noise(box[X1_D4], box[Y3_D4], box[Z2_D4]),
                input.noise(box[X1_D4], box[Y4_D4], box[Z2_D4]),
                input.noise(box[X2_D4], box[Y1_D4], box[Z2_D4]),
                input.noise(box[X2_D4], box[Y2_D4], box[Z2_D4]),
                input.noise(box[X2_D4], box[Y3_D4], box[Z2_D4]),
                input.noise(box[X2_D4], box[Y4_D4], box[Z2_D4]),
                input.noise(box[X3_D4], box[Y1_D4], box[Z2_D4]),
                input.noise(box[X3_D4], box[Y2_D4], box[Z2_D4]),
                input.noise(box[X3_D4], box[Y3_D4], box[Z2_D4]),
                input.noise(box[X3_D4], box[Y4_D4], box[Z2_D4]),
                input.noise(box[X4_D4], box[Y1_D4], box[Z2_D4]),
                input.noise(box[X4_D4], box[Y2_D4], box[Z2_D4]),
                input.noise(box[X4_D4], box[Y3_D4], box[Z2_D4]),
                input.noise(box[X4_D4], box[Y4_D4], box[Z2_D4]),
                input.noise(box[X1_D4], box[Y1_D4], box[Z3_D4]),
                input.noise(box[X1_D4], box[Y2_D4], box[Z3_D4]),
                input.noise(box[X1_D4], box[Y3_D4], box[Z3_D4]),
                input.noise(box[X1_D4], box[Y4_D4], box[Z3_D4]),
                input.noise(box[X2_D4], box[Y1_D4], box[Z3_D4]),
                input.noise(box[X2_D4], box[Y2_D4], box[Z3_D4]),
                input.noise(box[X2_D4], box[Y3_D4], box[Z3_D4]),
                input.noise(box[X2_D4], box[Y4_D4], box[Z3_D4]),
                input.noise(box[X3_D4], box[Y1_D4], box[Z3_D4]),
                input.noise(box[X3_D4], box[Y2_D4], box[Z3_D4]),
                input.noise(box[X3_D4], box[Y3_D4], box[Z3_D4]),
                input.noise(box[X3_D4], box[Y4_D4], box[Z3_D4]),
                input.noise(box[X4_D4], box[Y1_D4], box[Z3_D4]),
                input.noise(box[X4_D4], box[Y2_D4], box[Z3_D4]),
                input.noise(box[X4_D4], box[Y3_D4], box[Z3_D4]),
                input.noise(box[X4_D4], box[Y4_D4], box[Z3_D4]),
                input.noise(box[X1_D4], box[Y1_D4], box[Z4_D4]),
                input.noise(box[X1_D4], box[Y2_D4], box[Z4_D4]),
                input.noise(box[X1_D4], box[Y3_D4], box[Z4_D4]),
                input.noise(box[X1_D4], box[Y4_D4], box[Z4_D4]),
                input.noise(box[X2_D4], box[Y1_D4], box[Z4_D4]),
                input.noise(box[X2_D4], box[Y2_D4], box[Z4_D4]),
                input.noise(box[X2_D4], box[Y3_D4], box[Z4_D4]),
                input.noise(box[X2_D4], box[Y4_D4], box[Z4_D4]),
                input.noise(box[X3_D4], box[Y1_D4], box[Z4_D4]),
                input.noise(box[X3_D4], box[Y2_D4], box[Z4_D4]),
                input.noise(box[X3_D4], box[Y3_D4], box[Z4_D4]),
                input.noise(box[X3_D4], box[Y4_D4], box[Z4_D4]),
                input.noise(box[X4_D4], box[Y1_D4], box[Z4_D4]),
                input.noise(box[X4_D4], box[Y2_D4], box[Z4_D4]),
                input.noise(box[X4_D4], box[Y3_D4], box[Z4_D4]),
                input.noise(box[X4_D4], box[Y4_D4], box[Z4_D4]),
                normalize(box[X2_D4], box[X3_D4], x),
                normalize(box[Y2_D4], box[Y3_D4], y),
                normalize(box[Z2_D4], box[Z3_D4], z),
                tension, bias);
    }
}
