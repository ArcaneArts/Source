package art.arcane.source.noise.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class SeededProvider implements NoisePlaneProvider {
    protected final long seed;
}
