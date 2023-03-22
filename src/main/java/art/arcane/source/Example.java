package art.arcane.source;

import art.arcane.source.noise.NoiseTarget;
import art.arcane.source.noise.provider.*;
import art.arcane.source.util.MirroredFloatCache;
import art.arcane.source.util.NoisePreset;
import art.arcane.source.util.Weighted;
import lombok.Builder;
import lombok.Data;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

public class Example {
    // THIS IS A SIMPLE BIOME "MODEL" WHICH CONTAINS
    // * The Generator
    // * The name
    // * The weight (rarity), higher is more common
    @Data
    @Builder
    public static class Biome implements Weighted {
        private final NoisePlane generator;
        private final String name;
        private final double weight;
    }

    // We define a list of biomes with their weights and generators
    List<Biome> biomes = List.of(
            Biome.builder()
                    .name("Plains")
                    .weight(10)
                    .generator(new FlatProvider(0))
                    .build(),
            Biome.builder()
                    .name("Hills")
                    .weight(2)
                    .generator(new SimplexProvider(14))
                    .build()
    );

    private final NoisePlane masterGenerator;

    public Example()
    {
        // Initialize the master generator
        masterGenerator = new CellularProvider(1337).scale(0.25);
    }

    public double generateHeight(int x, int z)
    {
        // 1. Get the biome from the master generator
        Biome biome = masterGenerator.pickWeighted(x, z, biomes);

        // 2. Call it's generator
        return biome.getGenerator().noise(x, z);
    }
}
