package art.arcane.source.util;

import art.arcane.multiburst.MultiBurst;
import art.arcane.source.Source;
import art.arcane.source.noise.provider.CompiledProvider;
import art.arcane.source.noise.provider.ImageProvider;
import art.arcane.source.noise.provider.NoisePlaneProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheLoader {
    private List<CacheSector> sectors;
    private SourceIO.ImageChannel[] channels;
    private List<Runnable> work;

    public CacheLoader(SourceIO.ImageChannel... channels) {
        sectors = new ArrayList<>();
        this.channels = channels;
        this.work = new ArrayList<>();
    }

    public File layerFile(int layer)
    {
        File f = new File(Source.cacheDir, "L" + layer + ".png");
        f.getParentFile().mkdirs();
        return f;
    }

    public void export() {
        int k = 0;

        for(CacheSector i : sectors) {
            File f = layerFile(k++);
            try {
                ImageIO.write(i.getImage(), "png", f);
            }

            catch(Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void compile() {
        int l = work.size();
        PrecisionStopwatch p = PrecisionStopwatch.start();
        MultiBurst.burst.burst(work);
        work.clear();
        System.out.println("Compiled " + l + " Noise Layers into " + sectors.size() + " buffers in " + (int)p.getMilliseconds() + "ms");
        export();
    }

    public void register(CompiledProvider provider) {
        CacheSector sector = getNextSector(provider.getWidth(), provider.getHeight());
        SourceIO.ImageChannel c = sector.allocate();
        work.add(() -> {
            if(!sector.isLoaded())
            {
                SourceIO.write(sector.getImage(), c, provider.getGenerator());
            }

            provider.setCompiled(new ImageProvider(sector.getImage(), c));
        });
    }

    public CacheSector getNextSector(int width, int height) {
        for(CacheSector i : sectors) {
            if(i.getWidth() == width && i.getHeight() == height && i.isAvailable()) {
                return i;
            }
        }

        int l = sectors.size();
        File f = layerFile(l);
        CacheSector s = new CacheSector(width, height, channels);

        if(f.exists())
        {
            try {
                s.loadFrom(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        sectors.add(s);
        return s;
    }
}
