package art.arcane.source.noise;

import art.arcane.source.NoisePlane;
import art.arcane.source.noise.provider.NoisePlaneProvider;

public interface NoiseTarget {
    void collect(NoisePlane plane);
}
