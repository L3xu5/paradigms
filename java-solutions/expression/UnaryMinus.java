package expression;

import expression.generic.NumberWrapper;

public class UnaryMinus<U, T extends NumberWrapper<U, T>> extends AbstractUnaryOperation<U, T> {
    public UnaryMinus(ExpressionComponent<U, T> first) {
        super(first);
    }

    @Override
    public T evalOperation(T operand1) {
        if (operand1 == null) {
            return null;
        }
        return operand1.unaryMinus();
    }

    @Override
    public String getOperationSymbol() {
        return "unaryMinus";
    }

    @Override
    public int getPriority() {
        return 3;
    }

}
