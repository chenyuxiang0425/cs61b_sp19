import org.junit.Test;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int capacity) {
        T[] newitems = (T[]) new Object[capacity];
        int oldIndex = plusOne(nextFirst);
        for (int newIndex = 0; newIndex <= size; newIndex++) {
            newitems[newIndex] = items[oldIndex];
            oldIndex = plusOne(oldIndex);
        }
        items = newitems;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    private int minusOne(int index) {
        return (index - 1 + items.length) %  items.length;
    }

    private void upSize() {
        this.resize(size * 2);
    }

    private void downSize() {
        this.resize(items.length/2);
    }

    private boolean isFull() {
        return (items.length == size);
    }

    @Override
    public void addFirst(T item) {
        if (isFull()) {
            upSize();
        }
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (isFull()) {
            upSize();
        }
        items[nextLast] = item;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (size == items.length) {
            for (int i = plusOne(nextFirst); i < items.length; i = i + 1)
                System.out.print(items[i] + " ");
            for (int i = 0; i<nextLast;i =i+1) {
                System.out.print(items[i] + " ");
            }
        }

        for (int i=plusOne(nextFirst); i != nextLast;i=plusOne(i)) {
            System.out.print(items[i]+" ");
        }
    }

    @Override
    public T removeFirst() {
        if (items.length >= 16 && items.length >= 4 * size) {
            downSize();
        }

        nextFirst = plusOne(nextFirst);
        T item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        return item;

    }

    @Override
    public T removeLast() {
        if (items.length >= 16 && items.length >= 4 * size) {
            downSize();
        }

        nextLast = minusOne(nextLast);
        T item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return item;
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        int start = plusOne(nextFirst);
        return items[(start + index) % items.length];
    }


}
