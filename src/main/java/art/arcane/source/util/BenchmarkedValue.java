package art.arcane.source.util;

public class BenchmarkedValue<T> {
    private long score;
    private T object;

    public BenchmarkedValue(long score, T object) {
        this.score = score;
        this.object = object;
    }

    public long getScore() {
        return score;
    }

    public T getObject() {
        return object;
    }
}
