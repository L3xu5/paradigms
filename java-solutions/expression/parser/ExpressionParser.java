package expression.parser;

import expression.*;
import expression.generic.NumberWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class ExpressionParser<U, T extends NumberWrapper<U, T>> extends BaseParser {
    private final Class<T> wrapper;
    public ExpressionParser(CharSource source, Class<T> wrapper) {
        super(source);
        this.wrapper = wrapper;
    }

    public ExpressionParser(String source, Class<T> wrapper) {
        super(new StringSource(source));
        this.wrapper = wrapper;
    }

    private final Map<String, Integer> priorityOfOperation = Map.of("+", 0, "-", 0, "*", 1, "/", 1, "count", 2, "min", 0, "max", 0);

    public ExpressionComponent<U, T> parse() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespace();
        final ExpressionComponent<U, T> result = parseExpression();
        skipWhitespace();
        if (!eof()) {
            throw error("Expected end of file");
        }
        return result;
    }

    private ExpressionComponent<U, T> parseExpression() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExpressionComponent<U, T> left = parseUnary();
        if (eof() || take(')')) {
            return left;
        }
        skipWhitespace();
        String operation = parsePhrase();
        skipWhitespace();
        ExpressionComponent<U, T> right = parseExpression();
        skipWhitespace();
        return combine(left, right, operation);
    }

    private ExpressionComponent<U, T> parseUnary() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExpressionComponent<U, T> value;
        if (take('c')) {
            expect("ount");
            skipWhitespace();
            if (take('(')) {
                value = new Count<>(parseExpression());
                value.setInBrackets();
                skipWhitespace();
            } else {
                value = new Count<>(parseValue());
            }
        } else {
            value = parseValue();
        }
        return value;
    }

    private String parsePhrase() {
        StringBuilder result = new StringBuilder();
        while (!take(' ')) {
            result.append(take());
        }
        return result.toString();
    }

    private ExpressionComponent<U, T> combine(ExpressionComponent<U, T> left, ExpressionComponent<U, T> right, String operation) {
        if (priorityOfOperation.get(operation) > right.getPriority() && !right.isInBrackets()) {
            left = processOperation(left, right.getFirst(), operation);
            if (right.getSecond() == null) {
                return left;
            }
            return processOperation(left, right.getSecond(), right.getOperationSymbol());
        }
        return processOperation(left, right, operation);
    }

    private ExpressionComponent<U, T> processOperation(ExpressionComponent<U, T> left, ExpressionComponent<U, T> right, String operation) {
        return switch (operation) {
            case "+" -> new Add<>(left, right);
            case "-" -> new Subtract<>(left, right);
            case "*" -> new Multiply<>(left, right);
            case "/" -> new Divide<>(left, right);
            case "max" -> new Max<>(left, right);
            case "min" -> new Min<>(left, right);
            default -> throw error("Bad operation");
        };
    }

    private void takeInteger(final StringBuilder sb) {
        if (take('-')) {
            sb.append('-');
        }
        if (take('0')) {
            sb.append('0');
        } else if (between('1', '9')) {
            takeDigits(sb);
        } else if (sb.isEmpty()) {
            throw error("Invalid number: " + take());
        }
    }

    private T parseNumber(StringBuilder sb) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        takeInteger(sb);
        if (take('.')) {
            sb.append('.');
            takeDigits(sb);
        }
        if (take('e') || take('E')) {
            sb.append('e');
            if (take('+')) {
                // Do nothing
            } else if (take('-')) {
                sb.append('-');
            }
            takeDigits(sb);
        }
        return wrapper.getConstructor().newInstance().parseValue(sb.toString());

    }

    private ExpressionComponent<U, T> parseValue() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ExpressionComponent<U, T> value;
        boolean withUnaryMinus = take('-');
        if (take('(')) {
            skipWhitespace();
            value = parseExpression();
            value.setInBrackets();
            skipWhitespace();
        } else if (between('x', 'z')) {
            value = new Variable<>(Character.toString(take()));
        } else {
            if (withUnaryMinus) {
                return new Const<>(parseNumber(new StringBuilder("-")));
            }
            return new Const<>(parseNumber(new StringBuilder()));
        }
        skipWhitespace();
        if (withUnaryMinus) {
            return new UnaryMinus<>(value);
        }
        return value;
    }

    private void takeDigits(final StringBuilder sb) {
        while (between('0', '9')) {
            sb.append(take());
        }
    }


    private void skipWhitespace() {
        while (take(' ') || take('\r') || take('\n') || take('\t')) {
        }
    }
}
