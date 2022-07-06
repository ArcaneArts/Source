package art.arcane.source.interpolator;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.provider.NoisePlaneProvider;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true,
    fluent = true)
public abstract class Interpolator implements NoisePlaneProvider {
    protected final NoisePlane input;
    protected final double scale;
    protected final double iscale;
    
    public Interpolator(NoisePlane input, double scale)
    {
        this.input = input;
        this.scale = scale;
        this.iscale = 1.0 / scale;
    }

    public long getSeed()
    {
        if(input instanceof NoisePlaneProvider)
        {
            return ((NoisePlaneProvider) input).getSeed();
        }

        return -1;
    }

    public static final int X1_D2 = 0;
    public static final int X2_D2 = 1;
    public static final int Y1_D2 = 2;
    public static final int Y2_D2 = 3;
    public static final int Z1_D2 = 4;
    public static final int Z2_D2 = 5;
    public static final int X1_D4 = 0;
    public static final int X2_D4 = 1;
    public static final int X3_D4 = 2;
    public static final int X4_D4 = 3;
    public static final int Y1_D4 = 4;
    public static final int Y2_D4 = 5;
    public static final int Y3_D4 = 6;
    public static final int Y4_D4 = 7;
    public static final int Z1_D4 = 8;
    public static final int Z2_D4 = 9;
    public static final int Z3_D4 = 10;
    public static final int Z4_D4 = 11;

    /**
     * Gets the x1,x2,y1,y2,z1,z2
     * @param x cx coord
     * @param y cy coord
     * @param z cz coord
     * @return [x1,x2,y1,y2,z1,z2]
     */
    public int[] getScaleBoundsC3D2(double x, double y, double z)
    {
        int fx = getRadiusFactor(x);
        int fy = getRadiusFactor(y);
        int fz = getRadiusFactor(z);
        return new int[]{(int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale),
                (int) Math.round(fy * scale),
                (int) Math.round((fy + 1) * scale),
                (int) Math.round(fz * scale),
                (int) Math.round((fz + 1) * scale)};
    }

    /**
     * Gets the x1,x2,y1,y2
     * @param x cx coord
     * @param y cy coord
     * @return [x1,x2,y1,y2]
     */
    public int[] getScaleBoundsC2D2(double x, double y)
    {
        int fx = getRadiusFactor(x);
        int fy = getRadiusFactor(y);
        return new int[]{(int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale),
                (int) Math.round(fy * scale),
                (int) Math.round((fy + 1) * scale)};
    }

    /**
     * Gets the x1,x2
     * @param x cx coord
     * @return [x1,x2]
     */
    public int[] getScaleBoundsC1D2(double x)
    {
        int fx = getRadiusFactor(x);
        return new int[]{(int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale)};
    }

    /**
     * Gets the x1,x2,x3,x4,y1,y2,y3,y4,z1,z2,z3,z4
     * @param x cx coord
     * @return [x1,x2,x3,x4,y1,y2,y3,y4,z1,z2,z3,z4]
     */
    public int[] getScaleBoundsC3D4(double x, double y, double z)
    {
        int fx = getRadiusFactor(x);
        int fy = getRadiusFactor(y);
        int fz = getRadiusFactor(z);
        return new int[]{
                (int) Math.round((fx - 1) * scale),
                (int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale),
                (int) Math.round((fx + 2) * scale),
                (int) Math.round((fy - 1) * scale),
                (int) Math.round(fy * scale),
                (int) Math.round((fy + 1) * scale),
                (int) Math.round((fy + 2) * scale),
                (int) Math.round((fz - 1) * scale),
                (int) Math.round(fz * scale),
                (int) Math.round((fz + 1) * scale),
                (int) Math.round((fz + 2) * scale)};
    }

    /**
     * Gets the x1,x2,x3,x4,y1,y2,y3,y4
     * @param x cx coord
     * @return [x1,x2,x3,x4,y1,y2,y3,y4]
     */
    public int[] getScaleBoundsC2D4(double x, double y)
    {
        int fx = getRadiusFactor(x);
        int fy = getRadiusFactor(y);
        return new int[]{
                (int) Math.round((fx - 1) * scale),
                (int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale),
                (int) Math.round((fx + 2) * scale),
                (int) Math.round((fy - 1) * scale),
                (int) Math.round(fy * scale),
                (int) Math.round((fy + 1) * scale),
                (int) Math.round((fy + 2) * scale)};
    }

    /**
     * Gets the x1,x2,x3,x4
     * @param x cx coord
     * @return [x1,x2,x3,x4]
     */
    public int[] getScaleBoundsC1D4(double x)
    {
        int fx = getRadiusFactor(x);

        return new int[]{
                (int) Math.round((fx - 1) * scale),
                (int) Math.round(fx * scale),
                (int) Math.round((fx + 1) * scale),
                (int) Math.round((fx + 2) * scale)};
    }

    public static double rangeScale(double amin, double amax, double bmin, double bmax, double b) {
        return amin + ((amax - amin) * ((b - bmin) / (bmax - bmin)));
    }

    public double normalize(double bmin, double bmax, double b) {
        return (b - bmin) / (bmax - bmin);
    }
    
    public int getRadiusFactor(double coord)
    {
        if(scale > 1 && scale < 3) {return (int)coord >> 1;}
        if(scale > 3 && scale < 5) {return (int)coord >> 2;}
        if(scale > 7 && scale < 9) {return (int)coord >> 3;}
        if(scale > 15 && scale < 17) {return (int)coord >> 4;}
        if(scale > 31 && scale < 33) {return (int)coord >> 5;}
        if(scale > 63 && scale < 65) {return (int)coord >> 6;}
        if(scale > 127 && scale < 129) {return (int)coord >> 7;}
        if(scale > 255 && scale < 257) {return (int)coord >> 8;}
        if(scale > 511 && scale < 513) {return (int)coord >> 9;}
        if(scale > 1023 && scale < 1025) {return (int)coord >> 10;}
        return (int) Math.floor(coord * iscale);
    }

    public static double lerp(double a, double b, double f) {
        return a + (f * (b - a));
    }

    public static double blerp(double a, double b, double c, double d, double tx, double ty) {
        return lerp(lerp(a, b, tx), lerp(c, d, tx), ty);
    }

    public static double trilerp(double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8, double x, double y, double z) {
        return lerp(blerp(v1, v2, v3, v4, x, y), blerp(v5, v6, v7, v8, x, y), z);
    }

    public static double hermite(double p0, double p1, double p2, double p3, double mu, double tension, double bias) {
        double m0, m1, mu2, mu3;
        double a0, a1, a2, a3;

        mu2 = mu * mu;
        mu3 = mu2 * mu;
        m0 = (p1 - p0) * (1 + bias) * (1 - tension) / 2;
        m0 += (p2 - p1) * (1 - bias) * (1 - tension) / 2;
        m1 = (p2 - p1) * (1 + bias) * (1 - tension) / 2;
        m1 += (p3 - p2) * (1 - bias) * (1 - tension) / 2;
        a0 = 2 * mu3 - 3 * mu2 + 1;
        a1 = mu3 - 2 * mu2 + mu;
        a2 = mu3 - mu2;
        a3 = -2 * mu3 + 3 * mu2;

        return (a0 * p1 + a1 * m0 + a2 * m1 + a3 * p2);
    }

    public static double cubic(double p0, double p1, double p2, double p3, double mu) {
        double a0, a1, a2, a3, mu2;

        mu2 = mu * mu;
        a0 = p3 - p2 - p0 + p1;
        a1 = p0 - p1 - a0;
        a2 = p2 - p0;
        a3 = p1;

        return a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3;
    }

    public static double bicubic(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy) {
        //@builder
        return cubic(
                cubic(p00, p01, p02, p03, muy),
                cubic(p10, p11, p12, p13, muy),
                cubic(p20, p21, p22, p23, muy),
                cubic(p30, p31, p32, p33, muy),
                mux);
        //@done
    }

    public static double bihermite(double p00, double p01, double p02, double p03, double p10, double p11, double p12, double p13, double p20, double p21, double p22, double p23, double p30, double p31, double p32, double p33, double mux, double muy, double tension, double bias) {
        //@builder
        return hermite(
                hermite(p00, p01, p02, p03, muy, tension, bias),
                hermite(p10, p11, p12, p13, muy, tension, bias),
                hermite(p20, p21, p22, p23, muy, tension, bias),
                hermite(p30, p31, p32, p33, muy, tension, bias),
                mux, tension, bias);
        //@done
    }

    public static double trihermite(double p000, double p001, double p002, double p003, double p010, double p011, double p012, double p013, double p020, double p021, double p022, double p023, double p030, double p031, double p032, double p033, double p100, double p101, double p102, double p103, double p110, double p111, double p112, double p113, double p120, double p121, double p122, double p123, double p130, double p131, double p132, double p133, double p200, double p201, double p202, double p203, double p210, double p211, double p212, double p213, double p220, double p221, double p222, double p223, double p230, double p231, double p232, double p233, double p300, double p301, double p302, double p303, double p310, double p311, double p312, double p313, double p320, double p321, double p322, double p323, double p330, double p331, double p332, double p333, double mux, double muy, double muz, double tension, double bias) {
        //@builder
        return hermite(
                bihermite(p000, p001, p002, p003,
                        p010, p011, p012, p013,
                        p020, p021, p022, p023,
                        p030, p031, p032, p033,
                        mux, muy, tension, bias),
                bihermite(p100, p101, p102, p103,
                        p110, p111, p112, p113,
                        p120, p121, p122, p123,
                        p130, p131, p132, p133,
                        mux, muy, tension, bias),
                bihermite(p200, p201, p202, p203,
                        p210, p211, p212, p213,
                        p220, p221, p222, p223,
                        p230, p231, p232, p233,
                        mux, muy, tension, bias),
                bihermite(p300, p301, p302, p303,
                        p310, p311, p312, p313,
                        p320, p321, p322, p323,
                        p330, p331, p332, p333,
                        mux, muy, tension, bias),
                muz, tension, bias);
        //@done
    }

    public static double tricubic(double p000,
                                  double p001,
                                  double p002,
                                  double p003,
                                  double p010,
                                  double p011,
                                  double p012, double p013, double p020, double p021, double p022, double p023, double p030, double p031, double p032, double p033, double p100, double p101, double p102, double p103, double p110, double p111, double p112, double p113, double p120, double p121, double p122, double p123, double p130, double p131, double p132, double p133, double p200, double p201, double p202, double p203, double p210, double p211, double p212, double p213, double p220, double p221, double p222, double p223, double p230, double p231, double p232, double p233, double p300, double p301, double p302, double p303, double p310, double p311, double p312, double p313, double p320, double p321, double p322, double p323, double p330, double p331, double p332, double p333, double mux, double muy, double muz) {
        //@builder
        return cubic(
                bicubic(p000, p001, p002, p003,
                        p010, p011, p012, p013,
                        p020, p021, p022, p023,
                        p030, p031, p032, p033,
                        mux, muy),
                bicubic(p100, p101, p102, p103,
                        p110, p111, p112, p113,
                        p120, p121, p122, p123,
                        p130, p131, p132, p133,
                        mux, muy),
                bicubic(p200, p201, p202, p203,
                        p210, p211, p212, p213,
                        p220, p221, p222, p223,
                        p230, p231, p232, p233,
                        mux, muy),
                bicubic(p300, p301, p302, p303,
                        p310, p311, p312, p313,
                        p320, p321, p322, p323,
                        p330, p331, p332, p333,
                        mux, muy),
                muz);
        //@done
    }
}
