package art.arcane.source.noise.provider;

import art.arcane.source.util.FloatCache;
import art.arcane.source.util.MirroredFloatCache;
import art.arcane.source.util.SourceIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProvider implements NoisePlaneProvider {
    private final FloatCache cache;

    public ImageProvider(BufferedImage image, SourceIO.ImageChannel channel) {
        this.cache = SourceIO.load(image, channel);
    }

    public ImageProvider(File image, SourceIO.ImageChannel channel) throws IOException {
        this.cache = SourceIO.load(image, channel);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x, y);
    }

    @Override
    public double noise(double x, double y) {
        return cache.get((int)x, (int)y);
    }

    @Override
    public double noise(double x) {
        return noise(x, 0);
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public double getMinOutput() {
        return 0;
    }
}
