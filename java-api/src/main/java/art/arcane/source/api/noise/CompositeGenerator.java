package art.arcane.source.api.noise;

import art.arcane.source.api.NoisePlane;

import java.util.ArrayList;
import java.util.List;

public class CompositeGenerator implements NoisePlane {
    private final NoisePlane[] generators;
    private final CompositeMode mode;
    private final double minOutput;
    private final double maxOutput;

    public CompositeGenerator(CompositeMode mode, NoisePlane[] generators)
    {
        this.generators = generators;
        this.mode = mode;

        if(mode == CompositeMode.ADD)
        {
            int min = 0;
            int max = 0;
            for(NoisePlane i : generators)
            {
                min += i.getMinOutput();
                max+= i.getMaxOutput();
            }

            this.minOutput = min;
            this.maxOutput = max;
        }

        else {
            Double min = null;
            Double max = null;

            for(NoisePlane i : generators)
            {
                min = min == null ? i.getMinOutput() : Math.min(min, i.getMinOutput());
                max = max == null ? i.getMaxOutput() : Math.max(max, i.getMaxOutput());
            }

            this.minOutput = min;
            this.maxOutput = max;
        }
    }

    public double getMaxOutput()
    {
        return maxOutput;
    }

    public double getMinOutput()
    {
        return minOutput;
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
