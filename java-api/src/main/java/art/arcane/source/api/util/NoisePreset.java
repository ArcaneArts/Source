package art.arcane.source.api.util;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.fractal.FractalBillowProvider;
import art.arcane.source.api.fractal.FractalFBMProvider;
import art.arcane.source.api.fractal.FractalRigidMultiProvider;
import art.arcane.source.api.noise.Generator;
import art.arcane.source.api.noise.provider.CellularHeightProvider;
import art.arcane.source.api.noise.provider.CellularProvider;
import art.arcane.source.api.noise.provider.Cellularizer;
import art.arcane.source.api.noise.provider.CloverProvider;
import art.arcane.source.api.noise.provider.FlatProvider;
import art.arcane.source.api.noise.provider.PerlinProvider;
import art.arcane.source.api.noise.provider.SimplexProvider;
import art.arcane.source.api.noise.provider.WhiteProvider;

import java.util.function.Function;

public enum NoisePreset
{
    // RAW Noise Providers
    SIMPLEX(SimplexProvider::new),
    CELLULAR(CellularProvider::new),
    CELLULAR_HEIGHT(CellularHeightProvider::new),
    CLOVER(CloverProvider::new),
    FLAT(FlatProvider::new),
    PERLIN(PerlinProvider::new),
    WHITE(WhiteProvider::new),
    WETLAND((s) -> new FractalBillowProvider(PerlinProvider::new, s, 1, 0, 2D)),
    LAVA((s) -> new FractalBillowProvider(PerlinProvider::new, s, 3, 0.5, 2D)),
    SPATTER((s) -> new FractalFBMProvider(SimplexProvider::new, s, 4, 0.6, 2.75D)),
    THERMA((s) -> new SimplexProvider(s).octave(3, 0.5).warp(LAVA.create(s + 1), 0.75, 3).scale(4)),
    ;

    private final Function<Long, NoisePlane> plane;

    NoisePreset(Function<Long, NoisePlane> plane) {
        this.plane = plane;
    }

    public NoisePlane create(long seed) {
        return plane.apply(seed).scale(0.1);
    }
}
