package expression;

import expression.generic.NumberWrapper;

public abstract class AbstractUnaryOperation<U, T extends NumberWrapper<U, T>> implements ExpressionComponent<U, T> {
    private final ExpressionComponent<U, T> first;

    private boolean inBrackets = false;

    public AbstractUnaryOperation(ExpressionComponent<U, T> first) {
        this.first = first;
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
    public ExpressionComponent<U, T> getFirst() {
        return first;
    }

    public abstract T evalOperation(T operand1);

    public abstract String getOperationSymbol();

    @Override
    public abstract int getPriority();

    @Override
    public T evaluate(T x) {
        if (first == null) {
            return null;
        }
        return evalOperation(first.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        if (first == null) {
            return null;
        }
        return evalOperation(first.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return '(' + first.toString() + ' ' + getOperationSymbol() + ')';
    }

    @Override
    public int hashCode() {
        return (getFirst().hashCode() * 17 + getSecond().hashCode()) * 17 + String.valueOf(getOperationSymbol()).hashCode();
    }

    @Override
    public ExpressionComponent<U, T> getSecond() {
        return null;
    }
}
