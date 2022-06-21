package art.arcane.source.api.noise;

import art.arcane.source.api.accessor.ValueAccessor3D;

import java.util.ArrayList;
import java.util.List;

public class CompositeGenerator implements ValueAccessor3D {
    private final List<ValueAccessor3D> generators;
    private final CompositeMode mode;

    public CompositeGenerator(CompositeMode mode)
    {
        generators = new ArrayList<>();
        this.mode = mode;
    }

    public void add(ValueAccessor3D generator)
    {
        generators.add(generator);
    }

    @Override
    public double noise(double x) {
        double value = 0;

        for(ValueAccessor3D i : generators)
        {
            switch(mode)
            {
                case ADD, AVG -> value += i.noise(x);
                case MAX -> value = Math.max(i.noise(x), value);
                case MIN -> value = Math.min(i.noise(x), value);
            }
        }

        return mode == CompositeMode.AVG ? value / (generators.isEmpty() ? 1 : generators.size()) : value;
    }

    @Override
    public double noise(double x, double y) {
        return ValueAccessor3D.super.noise(x, y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return 0;
    }

    public enum CompositeMode
    {
        ADD,
        AVG,
        MAX,
        MIN
    }
}
