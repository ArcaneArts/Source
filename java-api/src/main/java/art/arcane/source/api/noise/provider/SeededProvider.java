package art.arcane.source.api.noise.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class SeededProvider implements NoisePlaneProvider {
    protected final long seed;
}
