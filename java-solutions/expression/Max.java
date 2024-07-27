package expression;

import expression.generic.NumberWrapper;

public class Max<U, T extends NumberWrapper<U, T>> extends AbstractBinaryOperation<U, T> {
    public Max(ExpressionComponent<U, T> first, ExpressionComponent<U, T> second) {
        super(first, second);
    }

    @Override
    public T evalOperation(T operand1, T operand2) {
        if (operand1 == null || operand2 == null) {
            return null;
        }
        return operand1.max(operand2);
    }

    @Override
    public String getOperationSymbol() {
        return "max";
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
