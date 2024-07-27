package expression;

import expression.generic.NumberWrapper;

public class Subtract<U, T extends NumberWrapper<U, T>> extends AbstractBinaryOperation<U, T> {

    public Subtract(ExpressionComponent<U, T> first, ExpressionComponent<U, T> second) {
        super(first, second);
    }

    @Override
    public T evalOperation(T operand1, T operand2) {
        if (operand1 == null || operand2 == null) {
            return null;
        }
        return operand1.subtract(operand2);
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }
    @Override
    public int getPriority() {
        return 0;
    }
}
