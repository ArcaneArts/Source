package art.arcane.source;

import art.arcane.source.ui.Visualizer;
import art.arcane.source.util.NoisePreset;
import java.io.IOException;

public class Example {
    public static void main(String[] a) throws IOException {
        NoisePlane plane =
            NoisePreset.BENDPLEX.create(3) // Base noise
                .posturize(9) // Split into 9 levels of height
                .scale(0.1) // Zome in
                .add(NoisePreset.WHITE.create(4).fit(-0.5, 0.5)) // Add noise to regain some detail
                .fit(-1, 1) // Remap to -1 to 1 due to adding noise
                .cachedMirror(512) // Cache it for performance
                .octave(5, 0.5) // Octaves to mix up the cache
                .scale(3)  // Zoom out for celluarization
                .cellularize(19) // Cellularize to create biomes
                .scale(0.25) // Scale for cosmetic after cells
                .fit(0, 1) // Refit
                .stretch(0.18, 0.8) // Stretch back to fix contrast
                .fit(-1, 1) // Final fit
            ;

        Visualizer.launch(plane);
    }
}
