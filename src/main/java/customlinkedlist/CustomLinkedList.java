package customlinkedlist;

public class CustomLinkedList<T> {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T val) {
            this.value = val;
        }
    }

    public void add(T element) {
        Node<T> node = new Node<>(element);
        if (head == null) head = node;
        else {
            Node<T> curr = head;
            while (curr.next != null) curr = curr.next;
            curr.next = node;
        }
        size++;
    }

    public T get(int index) {
        checkBounds(index);
        Node<T> curr = head;
        for (int i = 0; i < index; i++) curr = curr.next;
        return curr.value;
    }

    public T remove(int index) {
        checkBounds(index);
        if (index == 0) {
            T val = head.value;
            head = head.next;
            size--;
            return val;
        }
        Node<T> prev = head;
        for (int i = 0; i < index - 1; i++) prev = prev.next;
        T val = prev.next.value;
        prev.next = prev.next.next;
        size--;
        return val;
    }

    private void checkBounds(int idx) {
        if (idx < 0 || idx >= size)
            throw new IndexOutOfBoundsException("Index: " + idx);
    }

    public int size() {
        return size;
    }
}