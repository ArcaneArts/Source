package art.arcane.source;

import art.arcane.source.interpolator.CubicInterpolator;
import art.arcane.source.interpolator.HermiteInterpolator;
import art.arcane.source.interpolator.LinearInterpolator;
import art.arcane.source.interpolator.StarcastInterpolator;
import art.arcane.source.noise.provider.Cellularizer;
import art.arcane.source.noise.provider.ExponentProvider;
import art.arcane.source.noise.provider.FittedProvider;
import art.arcane.source.noise.provider.OctaveProvider;
import art.arcane.source.noise.provider.ScaledProvider;
import art.arcane.source.noise.provider.WarpedProvider;

public interface NoisePlane {
    default double noise(double x)
    {
        return noise(x, 0, 0);
    }

    default double noise(double x, double y)
    {
        return noise(x, y, 0);
    }


    double noise(double x, double y, double z);

    default CubicInterpolator cubic(double scale)
    {
        return new CubicInterpolator(this, scale);
    }

    default HermiteInterpolator hermite(double scale)
    {
        return new HermiteInterpolator(this, scale);
    }

    default LinearInterpolator linear(double scale)
    {
        return new LinearInterpolator(this, scale);
    }

    default StarcastInterpolator starcast(double scale, double checks)
    {
        return new StarcastInterpolator(this, scale, checks);
    }

    default StarcastInterpolator starcast(double scale)
    {
        return new StarcastInterpolator(this, scale, 9D);
    }

    default NoisePlane cellularize(long seed)
    {
        return new Cellularizer(this, seed);
    }

    default NoisePlane octave(int octaves, double gain)
    {
        return new OctaveProvider(this, octaves, gain);
    }

    default NoisePlane fit(double min, double max) {
        if(this instanceof FittedProvider s)
        {
            return new FittedProvider(s.getGenerator(), min, max);
        }
        return new FittedProvider(this, min, max);
    }
    default NoisePlane exponent(double exponent){
        return new ExponentProvider(this, exponent);
    }
    default NoisePlane scale(double scale) {
        if(this instanceof ScaledProvider s)
        {
            return new ScaledProvider(s.getGenerator(), s.getScale() * scale);
        }

        return new ScaledProvider(this, scale);
    }
    default NoisePlane warp(NoisePlane warp, double scale, double multiplier){
        return new WarpedProvider(this, warp, scale, multiplier);
    }

    default double getMaxOutput()
    {
        return 1;
    }

    default double getMinOutput()
    {
        return -1;
    }

    default void benchmark(String name, double ms)
    {
        double msb = ms/3;
        long t = System.currentTimeMillis();
        long d1 = 0;
        long d2 = 0;
        long d3 = 0;
        long n = 0;

        while(System.currentTimeMillis() - t < msb)
        {
           noise(n++);
           d1++;
        }

        t = System.currentTimeMillis();

        while(System.currentTimeMillis() - t < msb)
        {
            noise(n++, n++);
            d2++;
        }

        t = System.currentTimeMillis();

        while(System.currentTimeMillis() - t < msb)
        {
            noise(n++, n++, n++);
            d3++;
        }

        System.out.println(name + ": " + "1D: " + Math.round(d1/msb) + "/ms " + "2D: " + Math.round(d2/msb) + "/ms " + "3D: " + Math.round(d3/msb) + "/ms");
    }
}
