package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    public LinkedQueue() {
        head = tail = new Node();
    }


    @Override
    protected void enqueueImpl(Object element) {
        head.element = element;
        head.child = new Node(null, null);
        head = head.child;
    }

    @Override
    protected Object elementImpl() {
        return tail.element;
    }
    @Override
    protected Object dequeueImpl() {
        final Object result = tail.element;
        tail.element = null;
        tail = tail.child;
        return result;
    }

    @Override
    protected void clearImpl() {
        head = tail = new Node();
    }

    @Override
    protected Queue createQueue() {
        return new LinkedQueue();
    }

    private static class Node {
        private Object element;
        private Node child;

        public Node(Object element, Node child) {
            this.element = element;
            this.child = child;
        }
        public Node() {
            this.element = null;
            this.child = null;
        }
    }
}
