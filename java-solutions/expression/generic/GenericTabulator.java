package expression.generic;

import expression.ExpressionComponent;
import expression.parser.ExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class GenericTabulator implements Tabulator {
    private <U, T extends NumberWrapper<U, T>> Object[][][] processTabulation(String expression, int x1, int x2, int y1, int y2, int z1, int z2, Class<T> wrapper)  {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        try {
            ExpressionComponent<U, T> parsedExpression = (new ExpressionParser<>(expression, wrapper)).parse();
            Constructor<T> constructor = wrapper.getConstructor(int.class);
            for (int i = 0; i <= x2 - x1; i++) {
                for (int j = 0; j <= y2 - y1; j++) {
                    for (int k = 0; k <= z2 - z1; k++) {
                        NumberWrapper<U, T> evaluation = parsedExpression.evaluate(constructor.newInstance(x1 + i),
                                constructor.newInstance(y1 + j), constructor.newInstance(z1 + k));
                        if (evaluation == null) {
                            result[i][j][k] = null;
                        } else {
                            result[i][j][k] = evaluation.getValue();
                        }
                    }
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Bad wrapper was given: " + e);
        }
        return result;
    }


    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)  {
        return switch (mode) {
            case "i" -> processTabulation(expression, x1, x2, y1, y2, z1, z2, IntegerWrapper.class);
            case "d" -> processTabulation(expression, x1, x2, y1, y2, z1, z2, DoubleWrapper.class);
            case "u" -> processTabulation(expression, x1, x2, y1, y2, z1, z2, IntegerWithoutOverflowWrapper.class);
            case "b" -> processTabulation(expression, x1, x2, y1, y2, z1, z2, ByteWrapper.class);
            case "bi" ->
                    processTabulation(expression, x1, x2, y1, y2, z1, z2, BigIntegerWrapper.class);
            case "bool" ->
                    processTabulation(expression, x1, x2, y1, y2, z1, z2, BooleanWrapper.class);
            default -> throw new IllegalArgumentException("Bad type of value");
        };
    }
}
