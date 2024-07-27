package queue;

import java.util.Arrays;
import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i = 1..n: a[i] != null
// Let: immutable(k): forall i = 1..k: a'[i] = a[i]
public class ArrayQueueModule {
    private static int head;
    private static int tail;

    private static int size;
    private static Object[] elements = new Object[0];

    private static int moduleAdd(int value) {
        value++;
        if (value == elements.length) {
            return 0;
        }
        return value;
    }

    private static int moduleSub(int value) {
        if (value == 0) {
            return elements.length - 1;
        }
        return value - 1;
    }

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n)
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(++size);
        elements[head] = element;
        head = moduleAdd(head);
    }

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n')
    public static void push(Object element) {
        assert element != null;
        ensureCapacity(++size);
        tail = moduleSub(tail);
        elements[tail] = element;
    }

    // Pre: true
    // Post: n' = n && immutable(n)
    private static void ensureCapacity(int capacity) {
        if (capacity < elements.length) {
            return;
        }
        Object[] newElements = new Object[2 * capacity];
        if (head < tail) {
            System.arraycopy(elements, tail, newElements, 0, elements.length - tail);
            System.arraycopy(elements, 0, newElements, elements.length - tail, head);
        } else {
            System.arraycopy(elements, tail, newElements, 0, head - tail);
        }
        elements = newElements;
        head = size - 1;
        tail = 0;
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n &&
    //       immutable(n)
    public static Object element() {
        assert size() > 0;
        return elements[tail];
    }

    // Pre: n > 0
    // Post: R = a[n] && n' = n &&
    //       immutable(n)
    public static Object peek() {
        assert size > 0;
        return elements[moduleSub(head)];
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //       forall i = 1..n + 1: a'[i] = a[i + 1]
    public static Object dequeue() {
        assert size > 0;
        Object result = elements[tail];
        elements[tail] = null;
        tail = moduleAdd(tail);
        size--;
        return result;

    }

    // Pre: n > 0
    // Post: R = a[n] && n' = n - 1 && immutable(n)
    public static Object remove() {
        assert  size() > 0;
        head = moduleSub(head);
        Object result = elements[head];
        elements[head] = null;
        size--;
        return result;
    }

    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: R = (n == 0) && n' = n && immutable(n)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' = 0
    public static void clear() {
        elements = new Object[2];
        tail = head = 0;
        size = 0;
    }

    // Pre: predicate != null
    // Post: R = [elem for elem in a if predicate.test(elem)].length && n' = n && immutable(n)
    public static int countIf(Predicate<Object> predicate) {
        assert predicate != null;
        int result = 0;
        for (int i = tail; i != head; i = moduleAdd(i)) {
            if (predicate.test(elements[i])) {
                result++;
            }
        }
        return result;
    }
}
