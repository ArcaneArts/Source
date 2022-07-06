package art.arcane.source.noise.provider;

import art.arcane.source.NoisePlane;
import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;

public class ExpressionProvider implements NoisePlane {
    public static final Parser parser = new Parser();
    private final Expression expression;
    private final Scope scope;

    public ExpressionProvider(String expression) throws ParseException {
        this.scope = new Scope();
        this.expression = parser.parse(expression, scope);
    }

    @Override
    public double noise(double x) {
        return noise(x,0,0);
    }

    @Override
    public double noise(double x, double y) {
        return noise(x, y, 0);
    }

    @Override
    public double noise(double x, double y, double z) {
        scope.create("x", x);
        scope.create("y", y);
        scope.create("z", z);
        return expression.evaluate(x, y, z);
    }
}
