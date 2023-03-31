package art.arcane.source;

import art.arcane.source.interpolator.CubicInterpolator;
import art.arcane.source.interpolator.HermiteInterpolator;
import art.arcane.source.interpolator.LinearInterpolator;
import art.arcane.source.interpolator.StarcastInterpolator;
import art.arcane.source.noise.NoiseTarget;
import art.arcane.source.noise.provider.*;
import art.arcane.source.util.NoisePreset;
import art.arcane.source.util.SourceIO;
import art.arcane.source.util.Weighted;

import java.awt.image.BufferedImage;
import java.util.List;

public interface NoisePlane {
    default void fill(NoiseTarget target) {
        target.collect(this);
    }

    default NoisePlane slope(double radius) {
        return new Sloper(this, radius);
    }

    default NoisePlane gauss(double sigma) {
        return new GaussBlur(this, sigma);
    }

    default NoisePlane posturize(int values) {
        return new Posturizer(this, values);
    }

    default NoisePlane stretch(double min, double max) {
        return new Stretcher(this, min, max);
    }

    default MaxProvider max(NoisePlane other) {
        return new MaxProvider(this, other);
    }

    default MinProvider min(NoisePlane other) {
        return new MinProvider(this, other);
    }

    default AddingProvider add(NoisePlane other) {
        return new AddingProvider(this, other);
    }

    default SubtractingProvider subtract(NoisePlane other) {
        return new SubtractingProvider(this, other);
    }

    default MultiplyingProvider multiply(NoisePlane other) {
        return new MultiplyingProvider(this, other);
    }
    default NoisePlane multiply(double other) {
        return new FlatMultiplyingProvider(this, other);
    }

    default int i(double x, double y, double z, int min, int max)
    {
        return (int)Math.round(fit(min, max).noise(x,y,z));
    }

    default int i(double x, double y, int min, int max)
    {
        return (int)Math.round(fit(min, max).noise(x,y));
    }

    default int i(double x, int min, int max)
    {
        return (int)Math.round(fit(min, max).noise(x));
    }

    default double d(double x, double y, double z, double min, double max)
    {
        return fit(min, max).noise(x,y,z);
    }

    default double d(double x, double y, double min, double max)
    {
        return fit(min, max).noise(x,y);
    }

    default double d(double x, double min, double max)
    {
        return fit(min, max).noise(x);
    }

    default <T extends Weighted> T pickWeighted(double x, double y, double z, List<T> list)
    {
        double totalWeight = 0;
        double[] weights = new double[list.size()];
        T t;

        for(int i = 0; i < list.size(); i++) {
            t = list.get(i);
            weights[i] = t.getWeight();
            totalWeight += list.get(i).getWeight();
        }

        double r = d(x,y,z, 0, totalWeight);

        for(int i = 0; i < list.size(); i++) {
            if(r <= weights[i]) {
                return list.get(i);
            }

            r -= weights[i];
        }

        return list.get(list.size()-1);
    }

    default <T extends Weighted> T pickWeighted(double x, double y, List<T> list)
    {
        double totalWeight = 0;
        double[] weights = new double[list.size()];
        T t;

        for(int i = 0; i < list.size(); i++) {
            t = list.get(i);
            weights[i] = t.getWeight();
            totalWeight += list.get(i).getWeight();
        }

        double r = d(x,y, 0, totalWeight);

        for(int i = 0; i < list.size(); i++) {
            if(r <= weights[i]) {
                return list.get(i);
            }

            r -= weights[i];
        }

        return list.get(list.size()-1);
    }

    default <T extends Weighted> T pickWeighted(double x, List<T> list)
    {
        double totalWeight = 0;
        double[] weights = new double[list.size()];
        T t;

        for(int i = 0; i < list.size(); i++) {
            t = list.get(i);
            weights[i] = t.getWeight();
            totalWeight += list.get(i).getWeight();
        }

        double r = d(x, 0, totalWeight);

        for(int i = 0; i < list.size(); i++) {
            if(r <= weights[i]) {
                return list.get(i);
            }

            r -= weights[i];
        }

        return list.get(list.size()-1);
    }

    default <T> T pick(double x, double y, double z, List<T> list)
    {
        if(list.get(0) instanceof Weighted) {
            return (T)pickWeighted(x,y,z, (List<Weighted>)list);
        }

        return list.get(i(x,y,z, 0, list.size()-1));
    }

    default <T> T pick(double x, double y, List<T> list)
    {
        if(list.get(0) instanceof Weighted) {
            return (T)pickWeighted(x,y, (List<Weighted>)list);
        }

        return list.get(i(x,y, 0, list.size()-1));
    }

    default <T> T pick(double x, List<T> list)
    {
        if(list.get(0) instanceof Weighted) {
            return (T)pickWeighted(x, (List<Weighted>)list);
        }

        return list.get(i(x, 0, list.size()-1));
    }

    default <T> T pickArray(double x, T[] array)
    {
        return array[i(x, 0, array.length-1)];
    }

    default <T> T pickArray(double x, double y, T[] array)
    {
        return array[i(x, y, 0, array.length-1)];
    }

    /**
     * Inverts the noise value such that a value of 0 becomes 1, 1 becomes 0, and values in between are inverted.
     * @return The inverted noise plane.
     */
    default InvertedProvider invert() {
        return new InvertedProvider(this);
    }

    default DomainOffsetProvider offset(double x, double y, double z)
    {
        return new DomainOffsetProvider(this, x, y, z);
    }

    default DomainOffsetProvider offsetX(double x)
    {
        return new DomainOffsetProvider(this, x, 0, 0);
    }

    default DomainOffsetProvider offsetY(double y)
    {
        return new DomainOffsetProvider(this, 0, y, 0);
    }

    default DomainOffsetProvider offsetZ(double z)
    {
        return new DomainOffsetProvider(this, 0, 0, z);
    }

    default CacheProvider cached(int width, int height) {
        return new CacheProvider(this, width, height);
    }

    default MirroredCacheProvider cachedMirror(int width, int height) {
        return new MirroredCacheProvider(this, width, height);
    }

    default CacheProvider cached(int size) {
        return new CacheProvider(this, size, size);
    }

    default MirroredCacheProvider cachedMirror(int size) {
        return new MirroredCacheProvider(this, size, size);
    }

    default double noise(double x) {
        return noise(x, 0, 0);
    }

    default double noise(double x, double y) {
        return noise(x, y, 0);
    }

    double noise(double x, double y, double z);

    default CubicInterpolator cubic(double scale) {
        return new CubicInterpolator(this, scale);
    }

    default HermiteInterpolator hermite(double scale) {
        return new HermiteInterpolator(this, scale);
    }

    default LinearInterpolator linear(double scale) {
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
        if(min == getMinOutput() && max == getMaxOutput())
        {
            return this;
        }

        if(this instanceof FittedProvider s)
        {
            return new FittedProvider(s.getGenerator(), min, max);
        }
        return new FittedProvider(this, min, max);
    }

    default NoisePlane exponent(double exponent){
        return new ExponentProvider(this, exponent);
    }

    default NoisePlane edgeDetect(double percentChangeThreshold) {
        return edgeDetect(percentChangeThreshold, false);
    }

    default NoisePlane edgeDetect(double percentChangeThreshold, boolean fast) {
        return new EdgeDetector(this, percentChangeThreshold, fast);
    }

    default NoisePlane contrast(double amount)
    {
        return new Contrastor(this, amount);
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
