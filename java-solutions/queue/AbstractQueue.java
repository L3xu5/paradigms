package queue;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public void enqueue(Object element) {
        assert element != null;
        size++;
        enqueueImpl(element);
    }

    protected abstract void enqueueImpl(Object element);

    @Override
    public Object element() {
        assert size() > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected  abstract Object dequeueImpl();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    protected abstract Queue createQueue();

    @Override
    public Queue flatMap(Function<Object, List<Object>> function) {
        Queue queue = createQueue();
        for (int i = 0; i < size; i++) {
            Object element = dequeue();
            for (Object result: function.apply(element)) {
                queue.enqueue(result);
            }
            enqueue(element);
        }
        return queue;
    }
}
