package expression.generic;

public class IntegerWrapper extends NumberWrapper<Integer, IntegerWrapper> {
    public IntegerWrapper(Integer value) {
        super(value);
    }

    public IntegerWrapper(){
    }

    public IntegerWrapper(int value) {
        super(value);
    }

    @Override
    public IntegerWrapper parseValue(String value) {
        return new IntegerWrapper(Integer.parseInt(value));
    }

    @Override
    public IntegerWrapper add(IntegerWrapper other) {
        if (value == null || other == null || other.value == null) {
            return null;
        }
        int result = value + other.value;
        if (value > 0 && other.value > 0 && result < 0 || value < 0 && other.value < 0 && result >= 0) {
            return new IntegerWrapper(null);
        }
        return new IntegerWrapper(value + other.value);
    }

    @Override
    public IntegerWrapper subtract(IntegerWrapper other) {
        if (value == null || other == null|| other.value == null) {
            return null;
        }
        int result = value - other.value;
        if (value > 0 && other.value > 0 && result > value
                || value > 0 && other.value < 0 && result < value
                || value < 0 && other.value > 0 && result > value
                || value == 0 && other.value == Integer.MIN_VALUE) {
            return null;
        }
        return new IntegerWrapper(result);
    }

    @Override
    public IntegerWrapper divide(IntegerWrapper other) {
        if (value == null || other == null || other.value == null) {
            return null;
        }
        if (other.value == 0) {
            return null;
        }
        return new IntegerWrapper(value / other.value);
    }

    @Override
    public IntegerWrapper multiply(IntegerWrapper other) {
        if (value == null || other == null || other.value == null) {
            return null;
        }
        int result = value * other.value;
        if (value != 0 && other.value != 0 && (result / other.value != value || result / value != other.value)) {
            return null;
        }
        return new IntegerWrapper(value * other.value);
    }

    @Override
    public IntegerWrapper count() {
        if (value == null) {
            return null;
        }
        String bitString = Integer.toBinaryString(value);
        int result = 0;
        for (char ch : bitString.toCharArray()) {
            if (ch == '1') {
                result++;
            }
        }
        return new IntegerWrapper(result);
    }

    @Override
    public IntegerWrapper min(IntegerWrapper other) {
        if (value == null || other == null || other.value == null) {
            return null;
        }
        return new IntegerWrapper(Math.min(value, other.value));
    }

    @Override
    public IntegerWrapper max(IntegerWrapper other) {
        if (value == null || other == null || other.value == null) {
            return null;
        }
        return new IntegerWrapper(Math.max(value, other.value));
    }

    @Override
    public IntegerWrapper unaryMinus() {
        if (value == null || value == Integer.MIN_VALUE) {
            return null;
        }
        return new IntegerWrapper(-value);
    }
}
