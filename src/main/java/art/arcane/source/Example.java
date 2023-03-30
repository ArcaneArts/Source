package art.arcane.source;

import art.arcane.source.noise.NoiseTarget;
import art.arcane.source.noise.provider.*;
import art.arcane.source.ui.Visualizer;
import art.arcane.source.util.MirroredFloatCache;
import art.arcane.source.util.NoisePreset;
import art.arcane.source.util.Weighted;
import lombok.Builder;
import lombok.Data;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

public class Example {
    public static void main(String[] a) {
        NoisePlane plane = NoisePreset.SIMPLEX.create(0)
                .scale(0.01)
                .cachedMirror(1024)
                .warp(NoisePreset.PERLIN.create(1).cachedMirror(32).scale(0.01), 1, 1000)
                ;
        Visualizer.launch(plane);
    }
}
