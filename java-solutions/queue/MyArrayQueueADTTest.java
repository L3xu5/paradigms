package queue;

public class MyArrayQueueADTTest {
    public static void fill(ArrayQueueADT stack) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(stack, i);
        }
    }

    public static void dump(ArrayQueueADT stack, String prefix) {
        while (!ArrayQueueADT.isEmpty(stack)) {
            System.out.println(prefix + " " +
                    ArrayQueueADT.size(stack) + " " +
                            ArrayQueueADT.element(stack) + " " +
                            ArrayQueueADT.dequeue(stack)
            );
        }
    }

    public static void main(String[] args) {
        ArrayQueueADT stack1 = ArrayQueueADT.create();
        fill(stack1);
        System.out.println("Before clear stack №1: " + ArrayQueueADT.size(stack1));
        ArrayQueueADT.clear(stack1);
        System.out.println("After clear stack №1: " + ArrayQueueADT.size(stack1));
        fill(stack1);
        ArrayQueueADT stack2 = ArrayQueueADT.create();
        fill(stack2);
        System.out.println("Before clear stack №2: " + ArrayQueueADT.size(stack2));
        ArrayQueueADT.clear(stack2);
        System.out.println("After clear stack №2: " + ArrayQueueADT.size(stack2));
        fill(stack2);
        dump(stack1, "stack №1");
        dump(stack2, "stack №2");
    }
}
