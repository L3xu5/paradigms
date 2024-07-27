package expression.generic;

public class DoubleWrapper extends NumberWrapper<Double, DoubleWrapper> {
    public DoubleWrapper(Double value) {
        super(value);
    }

    public DoubleWrapper(){
    }

    public DoubleWrapper(int value) {
        super((double) value);
    }

    @Override
    public DoubleWrapper parseValue(String value) {
        return new DoubleWrapper(Double.parseDouble(value));
    }

    @Override
    public DoubleWrapper add(DoubleWrapper other) {
        return new DoubleWrapper(value + other.value);
    }

    @Override
    public DoubleWrapper subtract(DoubleWrapper other) {
        return new DoubleWrapper(value - other.value);
    }

    @Override
    public DoubleWrapper divide(DoubleWrapper other) {
        return new DoubleWrapper(value / other.value);
    }

    @Override
    public DoubleWrapper multiply(DoubleWrapper other) {
        return new DoubleWrapper(value * other.value);
    }

    @Override
    public DoubleWrapper count() {
        String bitString = Long.toBinaryString(Double.doubleToLongBits(value));
        double result = 0;
        for (char ch : bitString.toCharArray()) {
            if (ch == '1') {
                result++;
            }
        }
        return new DoubleWrapper(result);
    }

    @Override
    public DoubleWrapper min(DoubleWrapper other) {
        return new DoubleWrapper(Math.min(value, other.value));
    }

    @Override
    public DoubleWrapper max(DoubleWrapper other) {
        return new DoubleWrapper(Math.max(value, other.value));
    }

    @Override
    public DoubleWrapper unaryMinus() {
        return new DoubleWrapper(-value);
    }
}
