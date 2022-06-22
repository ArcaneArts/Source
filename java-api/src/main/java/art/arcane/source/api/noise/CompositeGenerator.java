package art.arcane.source.api.noise;

import art.arcane.source.api.NoisePlane;

import java.util.ArrayList;
import java.util.List;

public class CompositeGenerator implements NoisePlane {
    private final List<NoisePlane> generators;
    private final CompositeMode mode;

    public CompositeGenerator(CompositeMode mode)
    {
        generators = new ArrayList<>();
        this.mode = mode;
    }

    public void add(NoisePlane generator)
    {
        generators.add(generator);
    }

    @Override
    public double noise(double x) {
        double value = mode == CompositeMode.MAX ? -10 : mode == CompositeMode.MIN ? 10 : 0;

        for(NoisePlane i : generators)
        {
            switch(mode)
            {
                case ADD -> value += i.noise(x);
                case MAX -> value = Math.max(i.noise(x), value);
                case MIN -> value = Math.min(i.noise(x), value);
            }
        }

        return value;
    }

    @Override
    public double noise(double x, double y) {
        double value = mode == CompositeMode.MAX ? -10 : mode == CompositeMode.MIN ? 10 : 0;

        for(NoisePlane i : generators)
        {
            switch(mode)
            {
                case ADD -> value += i.noise(x, y);
                case MAX -> value = Math.max(i.noise(x, y), value);
                case MIN -> value = Math.min(i.noise(x, y), value);
            }
        }

        return value;
    }

    @Override
    public double noise(double x, double y, double z) {
        double value = mode == CompositeMode.MAX ? -10 : mode == CompositeMode.MIN ? 10 : 0;

        for(NoisePlane i : generators)
        {
            switch(mode)
            {
                case ADD -> value += i.noise(x, y, z);
                case MAX -> value = Math.max(i.noise(x, y, z), value);
                case MIN -> value = Math.min(i.noise(x, y, z), value);
            }
        }

        return value;
    }

    public enum CompositeMode
    {
        ADD,
        MAX,
        MIN
    }
}
