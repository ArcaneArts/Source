package art.arcane.source;

import art.arcane.source.ui.Visualizer;
import art.arcane.source.util.NoisePreset;
import java.io.IOException;

public class Example {
    public static void main(String[] a) throws IOException {

        NoisePlane plane =
            NoisePreset.SIMPLEX.create(1);
            ;

        Visualizer.launch(plane);
    }
}
