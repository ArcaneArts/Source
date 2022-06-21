package art.arcane.source.api.noise.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class SeededNoiseProvider implements NoiseProvider {
    protected final long seed;
}
