package expression.generic;

public class ByteWrapper extends NumberWrapper<Byte, ByteWrapper> {
    public ByteWrapper(Byte value) {
        super(value);
    }

    public ByteWrapper(){
    }

    public ByteWrapper(int value) {
        super((byte) value);
    }

    @Override
    public ByteWrapper parseValue(String value) {
        return new ByteWrapper(Byte.parseByte(value));
    }

    @Override
    public ByteWrapper add(ByteWrapper other) {
        return new ByteWrapper(value + other.value);
    }

    @Override
    public ByteWrapper subtract(ByteWrapper other) {
        return new ByteWrapper(value - other.value);
    }

    @Override
    public ByteWrapper divide(ByteWrapper other) {
        if (other.value == 0) {
            return null;
        }
        return new ByteWrapper(value / other.value);
    }

    @Override
    public ByteWrapper multiply(ByteWrapper other) {
        return new ByteWrapper(value * other.value);
    }

    @Override
    public ByteWrapper count() {
        String bitString = Integer.toBinaryString(value & 0xFF);
        int result = 0;
        for (char ch : bitString.toCharArray()) {
            if (ch == '1') {
                result++;
            }
        }
        return new ByteWrapper(result);
    }

    @Override
    public ByteWrapper min(ByteWrapper other) {
        return new ByteWrapper(Math.min(value, other.value));
    }

    @Override
    public ByteWrapper max(ByteWrapper other) {
        return new ByteWrapper(Math.max(value, other.value));
    }

    @Override
    public ByteWrapper unaryMinus() {
        return new ByteWrapper(-value);
    }
}
