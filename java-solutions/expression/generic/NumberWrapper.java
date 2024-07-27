package expression.generic;

public abstract class NumberWrapper<U, T extends NumberWrapper<U, T>> {
    protected U value;

    public NumberWrapper(U value) {
        this.value = value;
    }

    public NumberWrapper() {
    }

    public U getValue() {
        return value;
    }

    public abstract T parseValue(String value);

    public abstract T add(T other);

    public abstract T subtract(T other);

    public abstract T divide(T other);

    public abstract T multiply(T other);

    public abstract T count();

    public abstract T min(T other);

    public abstract T max(T other);

    public abstract T unaryMinus();
}
