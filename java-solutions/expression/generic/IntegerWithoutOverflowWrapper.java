package expression.generic;

public class IntegerWithoutOverflowWrapper extends NumberWrapper<Integer, IntegerWithoutOverflowWrapper> {
    public IntegerWithoutOverflowWrapper(Integer value) {
        super(value);
    }

    public IntegerWithoutOverflowWrapper(){
    }

    public IntegerWithoutOverflowWrapper(int value) {
        super(value);
    }

    @Override
    public IntegerWithoutOverflowWrapper parseValue(String value) {
        return new IntegerWithoutOverflowWrapper(Integer.parseInt(value));
    }

    @Override
    public IntegerWithoutOverflowWrapper add(IntegerWithoutOverflowWrapper other) {
        return new IntegerWithoutOverflowWrapper(value + other.value);
    }

    @Override
    public IntegerWithoutOverflowWrapper subtract(IntegerWithoutOverflowWrapper other) {
        return new IntegerWithoutOverflowWrapper(value - other.value);
    }

    @Override
    public IntegerWithoutOverflowWrapper divide(IntegerWithoutOverflowWrapper other) {
        if (other.value == 0) {
            return null;
        }
        return new IntegerWithoutOverflowWrapper(value / other.value);
    }

    @Override
    public IntegerWithoutOverflowWrapper multiply(IntegerWithoutOverflowWrapper other) {
        return new IntegerWithoutOverflowWrapper(value * other.value);
    }

    @Override
    public IntegerWithoutOverflowWrapper count() {
        String bitString = Integer.toBinaryString(value);
        int result = 0;
        for (char ch : bitString.toCharArray()) {
            if (ch == '1') {
                result++;
            }
        }
        return new IntegerWithoutOverflowWrapper(result);
    }

    @Override
    public IntegerWithoutOverflowWrapper min(IntegerWithoutOverflowWrapper other) {
        if (value <= other.value) {
            return this;
        }
        return other;
    }

    @Override
    public IntegerWithoutOverflowWrapper max(IntegerWithoutOverflowWrapper other) {
        if (value >= other.value) {
            return this;
        }
        return other;
    }

    @Override
    public IntegerWithoutOverflowWrapper unaryMinus() {
        return new IntegerWithoutOverflowWrapper(-value);
    }
}
