package art.arcane.source;

import art.arcane.source.noise.provider.ImageProvider;
import art.arcane.source.ui.Visualizer;
import art.arcane.source.util.NoisePreset;

import java.io.IOException;

public class Example {
    public static void main(String[] a) throws IOException {
        NoisePlane plane =
            NoisePreset.CELLULAR_EDGE_HAIRTHIN.create(0);
        ;

        plane.benchmarkPipeline2DPrint(1250);

        Visualizer.launch(plane);
    }
}
