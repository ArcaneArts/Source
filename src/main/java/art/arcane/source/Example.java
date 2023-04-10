package art.arcane.source;

import art.arcane.source.noise.provider.ImageProvider;
import art.arcane.source.ui.Visualizer;
import art.arcane.source.util.NoisePreset;
import art.arcane.source.util.SourceIO;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Example {
    public static void main(String[] a) throws IOException {
        NoisePlane plane = NoisePreset.NATURAL_FAST.create(10);

        Source.cacheLoader.compile();

        Visualizer.launch(plane);
    }
}
