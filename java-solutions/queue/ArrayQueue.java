package queue;

import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private int tail;
    private Object[] elements = new Object[0];

    private int moduleAdd(int value) {
        value++;
        if (value == elements.length) {
            return 0;
        }
        return value;
    }

    private int moduleSub(int value) {
        if (value == 0) {
            return elements.length - 1;
        }
        return value - 1;
    }

    private int getHead() {
        int head = tail + size - 1;
        if (head < elements.length) {
            return head;
        }
        return head - elements.length;
    }

    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(size);
        elements[getHead()] = element;
    }


    private void ensureCapacity(int capacity) {
        if (capacity < elements.length) {
            return;
        }
        Object[] newElements = new Object[2 * capacity];
        if (getHead() < tail) {
            System.arraycopy(elements, tail, newElements, 0, elements.length - tail);
            System.arraycopy(elements, 0, newElements, elements.length - tail, getHead());
        } else {
            System.arraycopy(elements, tail, newElements, 0, getHead() - tail);
        }
        elements = newElements;
        tail = 0;
    }

    @Override
    protected Object elementImpl() {
        return elements[tail];
    }

    @Override
    protected Object dequeueImpl() {
        Object result = elements[tail];
        elements[tail] = null;
        tail = moduleAdd(tail);
        return result;

    }

    @Override
    public void clearImpl() {
        elements = new Object[0];
        tail = 0;
    }

    @Override
    protected Queue createQueue() {
        return new ArrayQueue();
    }

    public void push(Object element) {
        assert element != null;
        ensureCapacity(++size);
        tail = moduleSub(tail);
        elements[tail] = element;
    }

    public Object peek() {
        assert size > 0;
        return elements[getHead()];
    }

    public Object remove() {
        assert  size > 0;
        Object result = elements[getHead()];
        elements[getHead()] = null;
        size--;
        return result;
    }

    public int countIf(Predicate<Object> predicate) {
        assert predicate != null;
        int result = 0;
        int idx = tail;
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[idx])) {
                result++;
            }
            idx = moduleAdd(idx);
        }
        return result;
    }
}
