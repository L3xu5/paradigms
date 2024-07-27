package expression;

import expression.generic.NumberWrapper;

public interface ExpressionComponent<U, T extends NumberWrapper<U, T>> {

    T evaluate(T x);
    T evaluate(T x, T y, T z);
    int getPriority();

    boolean isInBrackets();

    void setInBrackets();

    ExpressionComponent<U, T> getFirst();

    ExpressionComponent<U, T> getSecond();

   String getOperationSymbol();
}

