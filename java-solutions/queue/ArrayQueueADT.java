package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i = 1..n: a[i] != null
// Let: immutable(k): forall i = 1..k: a'[i] = a[i]
public class ArrayQueueADT {
    private int head;
    private int tail;
    private int size;
    private Object[] elements = new Object[0];

    // Pre: true
    // Post: R.n = 0
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // Pre: queue != null &&  element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n)

    private static int moduleAdd(ArrayQueueADT queue, int value) {
        value++;
        if (value == queue.elements.length) {
            return 0;
        }
        return value;
    }

    private static int moduleSub(ArrayQueueADT queue, int value) {
        if (value == 0) {
            return queue.elements.length - 1;
        }
        return value - 1;
    }

// :NOTE: pred and post conditions
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue,++queue.size);
        queue.elements[queue.head] = element;
        queue.head = moduleAdd(queue, queue.head);
    }

    // Pre: queue != null && element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n')
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, ++queue.size);
        queue.tail = moduleSub(queue, queue.tail);
        queue.elements[queue.tail] = element;
    }

    // Pre: queue != null
    // Post: n' = n && immutable(n)
    private static void ensureCapacity(ArrayQueueADT queue,int capacity) {
        if (capacity < queue.elements.length) {
            return;
        }
        Object[] newElements = new Object[2 * capacity];
        if (queue.head < queue.tail) {
            System.arraycopy(queue.elements, queue.tail, newElements, 0, queue.elements.length - queue.tail);
            System.arraycopy(queue.elements, 0, newElements, queue.elements.length - queue.tail, queue.head);
        } else {
            System.arraycopy(queue.elements, queue.tail, newElements, 0, queue.head - queue.tail);
        }
        queue.elements = newElements;
        queue.head = queue.size - 1;
        queue.tail = 0;
    }

    // Pre: queue != null && n > 0
    // Post: R = a[1] && n' = n &&
    //       immutable(n)
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.tail];
    }

    // Pre: queue != null && n > 0
    // Post: R = a[n] && n' = n &&
    //       immutable(n)
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[moduleSub(queue, queue.head)];
    }

    // Pre: queue != null && n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //       forall i = 1..n + 1: a'[i] = a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object result = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.tail = moduleAdd(queue, queue.tail);
        queue.size--;
        return result;

    }

    // Pre: queue != null && n > 0
    // Post: R = a[n] && n' = n - 1 && immutable(n)
    public static Object remove(ArrayQueueADT queue) {
        assert  queue.size > 0;
        queue.head = moduleSub(queue, queue.head);
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.size--;
        return result;
    }

    // Pre: queue != null
    // Post: R = n && n' = n && immutable(n)
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: queue != null
    // Post: R = (n == 0) && n' = n && immutable(n)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: queue != null &&
    // Post: n' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[1];
        queue.tail = queue.head = 0;
        queue.size = 0;
    }

    // Pre: queue != null && predicate != null
    // Post: R = [elem for elem in a if predicate.test(elem)].length && n' = n && immutable(n)
    public static int countIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        assert predicate != null;
        int result = 0;
        for (int i = queue.tail; i != queue.head; i = moduleAdd(queue, i)) {
            if (predicate.test(queue.elements[i])) {
                result++;
            }
        }
        return result;
    }
}
