package art.arcane.source.noise.provider;

import art.arcane.source.util.FloatCache;
import art.arcane.source.util.MirroredFloatCache;

import java.awt.image.BufferedImage;

public class ImageProvider implements NoisePlaneProvider{
    private final MirroredFloatCache image;

    public ImageProvider(MirroredFloatCache image) {
        this.image = image;
    }

    public static ImageProvider grayscale(BufferedImage image)
    {
        MirroredFloatCache cache = new MirroredFloatCache(image.getWidth(), image.getHeight());

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;
                double brightness = (r + g + b) / 3.0;
                cache.set(x, y, (float) (brightness / 255.0));
            }
        }

        return new ImageProvider(cache);
    }

    public static ImageProvider red(BufferedImage image)
    {
        MirroredFloatCache cache = new MirroredFloatCache(image.getWidth(), image.getHeight());

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                cache.set(x, y, (float) (r / 255.0));
            }
        }

        return new ImageProvider(cache);
    }

    public static ImageProvider green(BufferedImage image)
    {
        MirroredFloatCache cache = new MirroredFloatCache(image.getWidth(), image.getHeight());

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int g = (rgb >> 8) & 0xFF;
                cache.set(x, y, (float) (g / 255.0));
            }
        }

        return new ImageProvider(cache);
    }

    public static ImageProvider blue(BufferedImage image)
    {
        MirroredFloatCache cache = new MirroredFloatCache(image.getWidth(), image.getHeight());

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int b = (rgb) & 0xFF;
                cache.set(x, y, (float) (b / 255.0));
            }
        }

        return new ImageProvider(cache);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x, y);
    }

    @Override
    public double noise(double x, double y) {
        return image.get((int)x, (int)y);
    }

    @Override
    public double noise(double x) {
        return noise(x, 0);
    }

    @Override
    public long getSeed() {
        return 0;
    }
}
