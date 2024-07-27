package expression;

import expression.generic.NumberWrapper;

public class Variable<U, T extends NumberWrapper<U, T>> implements ExpressionComponent<U, T> {
    private final String name;

    private boolean inBrackets = false;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public T evaluate(T x) {
        return x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (this.name) {
            case "x" -> x;
            case "y" -> y;
            case  "z" -> z;
            default -> throw new IllegalStateException("Unexpected value: " + this.name);
        };
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isInBrackets() {
        return inBrackets;
    }

    @Override
    public void setInBrackets() {
        inBrackets = true;
    }


    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public ExpressionComponent<U, T> getFirst() {
        return this;
    }

    @Override
    public ExpressionComponent<U, T> getSecond() {
        return null;
    }

    @Override
    public String getOperationSymbol() {
        return null;
    }
}
