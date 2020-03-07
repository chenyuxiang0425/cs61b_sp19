public class LinkedListDeque<T> {
    private class _Node {
        _Node next;
        T item;
        _Node prev;
        public _Node(T item, _Node prev, _Node next) {
            this.next = next;
            this.item = item;
            this.prev = prev;
        }
    }

    _Node sentinel;
    int size;

    public LinkedListDeque() {
        sentinel = new _Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        sentinel.next = new _Node(item,sentinel,sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T item) {
        sentinel.prev = new _Node(item,sentinel.prev,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        _Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = size - 1;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = size - 1;
        return item;
    }

    public T get(int index) {
        if (size == 0) {
            return null;
        }
        int i = 0;
        _Node p = sentinel;
        while (i != index+1) {
            p = p.next;
            i = i + 1;
        }
        return p.item;
    }

    private T getRecursive(_Node p,int index, int start) {
        if (start == index+1) {
            return p.item;
        }
        return getRecursive(p.next,index,start+1);
    }

    public T getRecursive(int index) {
        return getRecursive(sentinel,index,0);
    }

    public void insert(T item,int position) {
        if (position == 0) {
            addFirst(item);
        }
        if (position >= this.size()) {
            addLast(item);
        }

        _Node p = sentinel;
        int  i= 1;
        while (i <= position) {
            p = p.next;
            i ++;
        }
        _Node tmp = p.next;
        p.next = new _Node(item,p,tmp);
        p.next.next.prev = p.next;
        tmp = null;
    }


}


