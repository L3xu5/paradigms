package queue;

import java.util.List;
import java.util.function.Function;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i = 1..n: a[i] != null
// Let: immutable(k): forall i = 1..k: a'[i] = a[i]
public interface Queue {

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n)
    void enqueue(Object element);

    // Pre: n > 0
    // Post: R = a[1] && n' = n &&
    //       immutable(n)
    Object element();

    // Pre: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //       forall i = 1..n + 1: a'[i] = a[i + 1]
    Object dequeue();

    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    int size();

    // Pre: true
    // Post: R = (n == 0) && n' = n && immutable(n)
    boolean isEmpty();

    // Pre: true
    // Post: n' = 0
    void clear();

    // Pre: function != null && function return not null List of not null object
    // Post: let x = [function(a[1]), function(a[2]), ... , function(a[n])]
    //      immutable(n) && R = [x[1][1], x[1][2], ... , x[1][n], x[2][1], x[2][2], ... , x[2][n],  ... ,
    //      x[n - 1][1], x[n - 1][2], ... , x[n - 1][n], x[n][1], x[n][2], ... , x[n][n]]
    Queue flatMap(Function<Object, List<Object>> function);
}
