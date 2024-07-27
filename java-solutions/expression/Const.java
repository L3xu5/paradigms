package expression;

import expression.generic.NumberWrapper;

public class Const<U, T extends NumberWrapper<U, T>> implements ExpressionComponent<U, T> {
    private final T x;

    private boolean inBrackets = false;

    public Const(T x) {
        this.x = x;
    }

    public T getX() {
        return this.x;
    }

    @Override
    public T evaluate(T x) {
        return this.x;
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
        return x.toString();
    }

    @Override
    public int hashCode() {
        return x.hashCode();
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

    @Override
    public T evaluate(T x, T y, T z) {
        return this.x;
    }
}
