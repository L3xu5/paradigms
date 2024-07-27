package expression.generic;

public class BooleanWrapper extends NumberWrapper<Boolean, BooleanWrapper> {
    public BooleanWrapper(){
    }

    public BooleanWrapper(Boolean value) {
        super(value);
    }

    public BooleanWrapper(int value) {
        super(value != 0);
    }

    @Override
    public BooleanWrapper parseValue(String value) {
        return new BooleanWrapper(Integer.parseInt(value));
    }

    @Override
    public BooleanWrapper add(BooleanWrapper other) {
        return new BooleanWrapper(Boolean.logicalOr(value, other.value));
    }

    @Override
    public BooleanWrapper subtract(BooleanWrapper other) {
        return new BooleanWrapper(Boolean.logicalXor(value, other.value));
    }

    @Override
    public BooleanWrapper divide(BooleanWrapper other) {
        if (other.value.equals(Boolean.FALSE)) {
            return null;
        }
        if (value.equals(Boolean.FALSE)) {
            return new BooleanWrapper(Boolean.FALSE);
        }
        return new BooleanWrapper(Boolean.TRUE);
    }

    @Override
    public BooleanWrapper multiply(BooleanWrapper other) {
        if (value.equals(Boolean.FALSE) || other.value.equals(Boolean.FALSE)) {
            return new BooleanWrapper(Boolean.FALSE);
        }
        return new BooleanWrapper(Boolean.TRUE);
    }

    @Override
    public BooleanWrapper count() {
        return this;
    }

    @Override
    public BooleanWrapper min(BooleanWrapper other) {
        return multiply(other);
    }

    @Override
    public BooleanWrapper max(BooleanWrapper other) {
        return add(other);
    }

    @Override
    public BooleanWrapper unaryMinus() {
        return this;
    }
}
