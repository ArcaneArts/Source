package art.arcane.source.util;

import art.arcane.multiburst.BurstExecutor;
import art.arcane.multiburst.MultiBurst;
import art.arcane.source.NoisePlane;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class NoiseCacheMap {
    private final ConcurrentLinkedHashMap<Long, FloatCache> map;
    private final int tileWidth;
    private final int tileHeight;
    private final int maxTiles;
    private NoisePlane noise;
    private AtomicInteger jobCode;

    public NoiseCacheMap(NoisePlane noise, int tileWidth, int tileHeight, int maxTiles) {
        this.noise = noise;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        jobCode = new AtomicInteger(0);
        this.maxTiles = maxTiles;
        this.map = new ConcurrentLinkedHashMap.Builder<Long, FloatCache>()
                .maximumWeightedCapacity(maxTiles)
                .build();
    }

    public void setNoise(NoisePlane noise) {
        this.noise = noise;
        clear();
    }

    public void render(FloatCache buffer, double xOff, double yOff) {
        BurstExecutor b = MultiBurst.burst.burst();

        for(int x = (int)xOff; x < (int)xOff + buffer.getWidth(); x+= tileWidth) {
            int finalX = x;
            for(int y = (int)yOff; y < (int)yOff + buffer.getHeight(); y+= tileHeight) {
                int finalY = y;
                b.queue(()-> {
                    FloatCache cache = getTile(finalX, finalY, -1);
                    for(int i = 0; i < tileWidth; i++) {
                        for(int j = 0; j < tileHeight; j++) {
                            buffer.set((int) (finalX - xOff) + i, (int) (finalY - yOff) + j, cache.get(i, j));
                        }
                    }
                });
            }
        }

        b.complete();
    }

    public void precacheParallel(int minX, int minY, int maxX, int maxY, BurstExecutor executor) {
        int code = jobCode.incrementAndGet();
        for(int x = minX-tileWidth; x < maxX+tileWidth; x+= tileWidth) {
            int tx = (int) Math.floor(x / tileWidth);

            for(int y = minY-tileHeight; y < maxY+tileHeight; y+= tileHeight) {
                int ty = (int) Math.floor(y / tileHeight);
                executor.queue(() -> getTile(tx, ty, code));
            }
        }
    }

    public void clear() {
        map.clear();
    }

    private long key(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

    public double getValueOr(double x, double y, double value) {
        int tx = (int) Math.floor(x / tileWidth);
        int ty = (int) Math.floor(y / tileHeight);
        FloatCache c = getTileOrNull(tx, ty);

        if(c == null) {
            return value;
        }

        return c.get((int) (x - (tx * tileWidth)), (int) (y - (ty * tileHeight)));
    }

    public double getValue(double x, double y) {
        int tx = (int) Math.floor(x / tileWidth);
        int ty = (int) Math.floor(y / tileHeight);
        FloatCache c = getTile(tx, ty, -1);
        return c.get((int) (x - (tx * tileWidth)), (int) (y - (ty * tileHeight)));
    }

    public FloatCache getTileOrNull(int x, int y){
        return map.get(key(x, y));
    }

    public FloatCache getTile(int x, int y, int code) {
        return map.computeIfAbsent(key(x, y), (k) -> buildFloatCache(x, y, code));
    }

    private FloatCache buildFloatCache(int tx, int ty, int code) {
        FloatCache c = new FloatCache(tileWidth, tileHeight);
        int x = tx * tileWidth;
        int y = ty * tileHeight;

        for(int i = 0; i < tileWidth; i++) {
            for(int j = 0; j < tileHeight; j++) {
                if(code > 0 && code != jobCode.get()) {
                    break;
                }

                c.set(i, j, (float)noise.noise(x + i, y + j));
            }
        }

        return c;
    }
}
