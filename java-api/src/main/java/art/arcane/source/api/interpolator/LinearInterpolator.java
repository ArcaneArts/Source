package art.arcane.source.api.interpolator;

import art.arcane.source.api.accessor.ValueAccessor3D;

import java.util.Arrays;

public class LinearInterpolator extends Interpolator
{
    public LinearInterpolator(ValueAccessor3D input, double scale) {
        super(input, scale);
    }

    @Override
    public double noise(double x) {
        int[] box = getScaleBoundsC1D2(x);
        return lerp(
                input.noise(box[X1_D2]),
                input.noise(box[X2_D2]),
                normalize(box[X1_D2], box[X2_D2], x));
    }

    @Override
    public double noise(double x, double y) {
        int[] box = getScaleBoundsC2D2(x, y);

        if(Math.random() > 0.99)
        {
            System.out.println("For " + x + " " + y + " box is " + Arrays.toString(box));
        }

        return blerp(
                input.noise(box[X1_D2], box[Y1_D2]),
                input.noise(box[X2_D2], box[Y1_D2]),
                input.noise(box[X1_D2], box[Y2_D2]),
                input.noise(box[X2_D2], box[Y2_D2]),
                normalize(box[X1_D2], box[X2_D2], x),
                normalize(box[Y1_D2], box[Y2_D2], y));
    }

    @Override
    public double noise(double x, double y, double z) {
        int[] box = getScaleBoundsC3D2(x, y, z);
        return trilerp(
                input.noise(box[X1_D2], box[Y1_D2], box[Z1_D2]),
                input.noise(box[X2_D2], box[Y1_D2], box[Z1_D2]),
                input.noise(box[X1_D2], box[Y2_D2], box[Z1_D2]),
                input.noise(box[X2_D2], box[Y2_D2], box[Z1_D2]),
                input.noise(box[X1_D2], box[Y1_D2], box[Z2_D2]),
                input.noise(box[X2_D2], box[Y1_D2], box[Z2_D2]),
                input.noise(box[X1_D2], box[Y2_D2], box[Z2_D2]),
                input.noise(box[X2_D2], box[Y2_D2], box[Z2_D2]),
                normalize(box[X1_D2], box[X2_D2], x),
                normalize( box[Y1_D2], box[Y2_D2], y),
                normalize(box[Z1_D2], box[Z2_D2], z));
    }
}
