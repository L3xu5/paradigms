package expression;

import expression.generic.NumberWrapper;

public class Count<U, T extends NumberWrapper<U, T>> extends AbstractUnaryOperation<U, T> {
    public Count(ExpressionComponent<U, T> first) {
        super(first);
    }

    @Override
    public T evalOperation(T operand1) {
        if (operand1 == null) {
            return null;
        }
        return operand1.count();
    }

    @Override
    public String getOperationSymbol() {
        return "count";
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
