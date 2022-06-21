package art.arcane.source.api.interpolator;

import art.arcane.source.api.accessor.ValueAccessor3D;

public class CubicInterpolator extends Interpolator
{
    public CubicInterpolator(ValueAccessor3D input, double scale) {
        super(input, scale);
    }

    @Override
    public double noise(double x) {
        int[] box = getScaleBoundsC1D4(x);
        return cubic(
                input.noise(box[X1_D4]),
                input.noise(box[X2_D4]),
                input.noise(box[X3_D4]),
                input.noise(box[X4_D4]),
                normalize(box[X2_D4], box[X3_D4], x));
    }

    @Override
    public double noise(double x, double y) {
        int[] box = getScaleBoundsC2D4(x, y);
        return bicubic(
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
                normalize(box[Y2_D4], box[Y3_D4], y));
    }

    @Override
    public double noise(double x, double y, double z) {
        int[] box = getScaleBoundsC3D4(x, y, z);
        return tricubic(
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
                normalize(box[Z2_D4], box[Z3_D4], z));
    }
}
