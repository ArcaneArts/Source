package art.arcane.source.api;

import art.arcane.source.api.noise.provider.ExponentProvider;
import art.arcane.source.api.noise.provider.FittedProvider;
import art.arcane.source.api.noise.provider.OctaveProvider;
import art.arcane.source.api.noise.provider.ScaledProvider;
import art.arcane.source.api.noise.provider.WarpedProvider;

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
