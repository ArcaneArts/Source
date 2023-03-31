package art.arcane.source.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SourceIO {
    public static enum ImageChannel {
        RED, GREEN, BLUE, HUE, SATURATION, BRIGHTNESS, GRAYSCALE
    }

    public static FloatCache load(File image, ImageChannel channel) throws IOException {
        return load(ImageIO.read(image), channel);
    }

    public static void save(BufferedImage image, ImageChannel channel, FloatCache cache) {
        for(int i = 0; i < image.getWidth(); i++) {
            for(int j = 0; j < image.getHeight(); j++) {
                float v = cache.get(i, j);
                int rgb = 0;

                switch(channel) {
                    case RED -> {
                        rgb = (int) (v * 255f);
                        rgb = (rgb << 8) | 0xFF;
                        rgb = (rgb << 8) | 0xFF;
                        rgb = (rgb << 8) | 0xFF;
                    }
                    case GREEN -> {
                        rgb = (int) (v * 255f);
                        rgb = (rgb << 8) | 0xFF;
                        rgb = (rgb << 8) | 0xFF;
                    }
                    case BLUE -> {
                        rgb = (int) (v * 255f);
                        rgb = (rgb << 8) | 0xFF;
                    }
                    case HUE -> rgb = Color.HSBtoRGB(v, 1, 1);
                    case SATURATION -> rgb = Color.HSBtoRGB(0, v, 1);
                    case BRIGHTNESS -> rgb = Color.HSBtoRGB(0, 1, v);
                    case GRAYSCALE -> {
                        rgb = (int) (v * 255f);
                        rgb = (rgb << 8) | (int) (v * 255f);
                        rgb = (rgb << 8) | (int) (v * 255f);
                        rgb = (rgb << 8) | 0xFF;
                    }
                }

                image.setRGB(i, j, rgb);
            }
        }
    }

    public static FloatCache load(BufferedImage image, ImageChannel channel) {
        FloatCache f = new MirroredFloatCache(image.getWidth(), image.getHeight());
        for(int i = 0; i < image.getWidth(); i++) {
            for(int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;
                float[] hsb = new float[3];
                Color.RGBtoHSB(r, g, b, hsb);
                float v = switch(channel) {
                    case RED -> r / 255f;
                    case GREEN -> g / 255f;
                    case BLUE -> b / 255f;
                    case HUE -> hsb[0];
                    case SATURATION -> hsb[1];
                    case BRIGHTNESS -> hsb[2];
                    case GRAYSCALE -> (r + g + b) / 3f / 255f;
                };

                f.set(i, j, v);
            }
        }

        return f;
    }
}
