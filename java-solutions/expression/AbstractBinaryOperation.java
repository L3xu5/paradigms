package expression;

import expression.generic.NumberWrapper;

public abstract class AbstractBinaryOperation<U, T extends NumberWrapper<U, T>> implements ExpressionComponent<U, T> {
    private final ExpressionComponent<U, T> first;
    private final ExpressionComponent<U, T> second;

    private boolean inBrackets = false;

    public AbstractBinaryOperation(ExpressionComponent<U, T> first, ExpressionComponent<U, T> second) {
        this.first = first;
        this.second = second;
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

    @Override
    public ExpressionComponent<U, T> getSecond() {
        return second;
    }

    public abstract T evalOperation(T operand1, T operand2);

    public abstract String getOperationSymbol();

    @Override
    public abstract int getPriority();

    @Override
    public T evaluate(T x) {
        if (first != null && second != null) {
            return evalOperation(first.evaluate(x), second.evaluate(x));
        }
        if (first != null) {
            return evalOperation(first.evaluate(x), null);
        }
        if (second != null) {
            return evalOperation(null, second.evaluate(x));
        }
        return null;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        if (first != null && second != null) {
            return evalOperation(first.evaluate(x, y, z), second.evaluate(x, y, z));
        }
        if (first != null) {
            return evalOperation(first.evaluate(x, y, z), null);
        }
        if (second != null) {
            return evalOperation(null, second.evaluate(x, y, z));
        }
        return null;
    }

    @Override
    public String toString() {
        return '(' + first.toString() + ' ' + getOperationSymbol() + ' ' + second.toString() + ')';
    }

    @Override
    public int hashCode() {
        return (getFirst().hashCode() * 17 + getSecond().hashCode()) * 17 + String.valueOf(getOperationSymbol()).hashCode();
    }
}
