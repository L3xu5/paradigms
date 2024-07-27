package expression.generic;

import java.math.BigInteger;
import java.util.Objects;

public class BigIntegerWrapper extends NumberWrapper<BigInteger, BigIntegerWrapper> {
    public BigIntegerWrapper(BigInteger value) {
        super(value);
    }

    public BigIntegerWrapper() {
    }

    public BigIntegerWrapper(int value) {
        super(BigInteger.valueOf(value));
    }

    @Override
    public BigIntegerWrapper parseValue(String value) {
        return new BigIntegerWrapper(new BigInteger(value));
    }

    @Override
    public BigIntegerWrapper add(BigIntegerWrapper other) {
        return new BigIntegerWrapper(value.add(other.value));
    }

    @Override
    public BigIntegerWrapper subtract(BigIntegerWrapper other) {
        return new BigIntegerWrapper(value.subtract(other.value));
    }

    @Override
    public BigIntegerWrapper divide(BigIntegerWrapper other) {
        if (Objects.equals(other.value, BigInteger.ZERO)) {
            return null;
        }
        return new BigIntegerWrapper(value.divide(other.value));
    }

    @Override
    public BigIntegerWrapper multiply(BigIntegerWrapper other) {
        return new BigIntegerWrapper(value.multiply(other.value));
    }

    @Override
    public BigIntegerWrapper count() {
        return new BigIntegerWrapper(value.bitCount());
    }

    @Override
    public BigIntegerWrapper min(BigIntegerWrapper other) {
        return new BigIntegerWrapper(value.min(other.value));
    }

    @Override
    public BigIntegerWrapper max(BigIntegerWrapper other) {
        return new BigIntegerWrapper(value.max(other.value));
    }

    @Override
    public BigIntegerWrapper unaryMinus() {
        return new BigIntegerWrapper(value.negate());
    }
}
