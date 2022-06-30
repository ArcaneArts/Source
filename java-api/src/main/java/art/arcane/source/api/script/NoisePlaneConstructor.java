package art.arcane.source.api.script;

import art.arcane.source.api.NoisePlane;
import art.arcane.source.api.noise.provider.CellularHeightProvider;
import art.arcane.source.api.noise.provider.CellularProvider;
import art.arcane.source.api.noise.provider.Cellularizer;
import art.arcane.source.api.noise.provider.CloverProvider;
import art.arcane.source.api.noise.provider.ExponentProvider;
import art.arcane.source.api.noise.provider.ExpressionProvider;
import art.arcane.source.api.noise.provider.FlatProvider;
import art.arcane.source.api.noise.provider.NoisePlaneProvider;
import art.arcane.source.api.noise.provider.PerlinProvider;
import art.arcane.source.api.noise.provider.SimplexProvider;
import art.arcane.source.api.noise.provider.WhiteProvider;
import art.arcane.source.api.util.NoisePreset;
import com.dfsek.paralithic.eval.tokenizer.ParseException;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.List;
import java.util.function.Function;

public class NoisePlaneConstructor {
    public static NoisePlane execute(long seed, String script) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();

        List<ScriptEngineFactory> factories = sem.getEngineFactories();
        for (ScriptEngineFactory factory : factories)
            System.out.println(factory.getEngineName() + " " + factory.getEngineVersion() + " " + factory.getNames());
        if (factories.isEmpty())
            System.out.println("No Script Engines found");

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("java");
        engine.put("seed", seed);
        engine.put("Noise", new NoisePlaneConstructor());

        return (NoisePlane) engine.eval(script);
    }

    public NoisePlane call(String script, long seed)
    {
        try {
            return execute(seed, script);
        } catch(ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Function<Long, NoisePlane> builder(String script)
    {
        return (s) -> call(script, s);
    }

    public NoisePlane expression(String expression)
    {
        try {
            return new ExpressionProvider(expression);
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public NoisePlane clover(long seed)
    {
        return new CloverProvider(seed);
    }

    public NoisePlane flat(long seed)
    {
        return new FlatProvider(seed);
    }

    public NoisePlane perlin(long seed)
    {
        return new PerlinProvider(seed);
    }

    public NoisePlane simplex(long seed)
    {
        return new SimplexProvider(seed);
    }

    public NoisePlane white(long seed)
    {
        return new WhiteProvider(seed);
    }

    public NoisePlane cellular(long seed)
    {
        return new CellularProvider(seed);
    }

    public NoisePlane cellularHeight(long seed)
    {
        return new CellularHeightProvider(seed);
    }
}