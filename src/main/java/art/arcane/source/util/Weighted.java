package art.arcane.source.util;

public interface Weighted {
    default double getWeight() {
        return 1;
    }
}
