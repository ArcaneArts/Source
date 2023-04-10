package art.arcane.source;

import art.arcane.source.util.CacheLoader;
import art.arcane.source.util.SourceIO;

import java.io.File;

public class Source {
    public static File cacheDir = new File("cache");
    public static CacheLoader cacheLoader = new CacheLoader(
            SourceIO.ImageChannel.RED,
            SourceIO.ImageChannel.GREEN,
            SourceIO.ImageChannel.BLUE
    );
}
