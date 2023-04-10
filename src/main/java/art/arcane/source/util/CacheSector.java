package art.arcane.source.util;

import art.arcane.source.Source;
import art.arcane.source.noise.provider.CompiledProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class CacheSector {
    private final int width;
    private final int height;
    private int allocations;
    private final SourceIO.ImageChannel[] channels;
    private BufferedImage image;
    private boolean loaded;

    public CacheSector(int width, int height, SourceIO.ImageChannel... channels) {
        this.width = width;
        this.height = height;
        this.channels = channels;
        this.allocations = 0;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.loaded = false;
    }

    public boolean isAvailable() {
        return allocations < channels.length;
    }

    public SourceIO.ImageChannel allocate() {
        allocations++;
        return channels[allocations - 1];
    }

    public void loadFrom(File f) throws IOException {
        image = ImageIO.read(f);
        loaded = true;
    }
}
