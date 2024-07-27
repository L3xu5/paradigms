package queue;

public class MyArrayQueueTest {
    public static void fill(ArrayQueue queue) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
    }

    public static void dump(ArrayQueue queue, String prefix) {
        while (!queue.isEmpty()) {
            System.out.println(
                    prefix + " " +
                    queue.size() + " " +
                            queue.element() + " " +
                            queue.dequeue()
            );
        }
    }

    public static void main(String[] args) {
        ArrayQueue stack1 = new ArrayQueue();
        fill(stack1);
        System.out.println("Before clear stack №1: " + stack1.size());
        stack1.clear();
        System.out.println("After clear stack №1: " + stack1.size());
        fill(stack1);
        ArrayQueue stack2 = new ArrayQueue();
        fill(stack2);
        System.out.println("Before clear stack №2: " + stack2.size());
        stack2.clear();
        System.out.println("After clear stack №2: " + stack2.size());
        fill(stack2);
        dump(stack1, "stack №1");
        dump(stack2, "stack №2");
    }
}
