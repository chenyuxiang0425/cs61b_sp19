package bearmaps;

import java.security.Key;
import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private int size;
    private List<PriorityNode> pq;
    private Map<T,Integer> itemMapIndex;

    public ArrayHeapMinPQ() {
        pq = new ArrayList<>();
        size = 0;
        pq.add(null);
        itemMapIndex = new HashMap();
        itemMapIndex.put(null,0);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) throw new IllegalArgumentException();
        PriorityNode pqNode = new PriorityNode(item, priority);
        pq.add(pqNode);
        size += 1;
        itemMapIndex.put(item,size);
        swim(size);
    }

    @Override
    public boolean contains(T item) {
        // TODO
        return itemMapIndex.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size() == 0) throw new NoSuchElementException();
        return pq.get(1).item;
    }

    @Override
    public T removeSmallest() {
        if (size() == 0) throw new NoSuchElementException();
        T toRemove = pq.get(1).item;
        exch(1,size);
        pq.remove(size);
        itemMapIndex.remove(toRemove);
        size -= 1;
        sink(1);
        return toRemove;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (size() == 0 || !contains(item)) throw new NoSuchElementException();
        int index = itemMapIndex.get(item);
        PriorityNode toChange = pq.get(index);
        Double OldPriority = toChange.getPriority();
        toChange.setPriority(priority);
        if (OldPriority < priority) { //priority is down
            sink(index);
        } else {
            swim(index);
        }
    }

    // going up
    private void swim(int index) {
        if (index == 1) return; // root
        while (index > 1 && greater(pq.get(index/2),pq.get(index))) {
            exch(index,index/2);
            index = index /2;
        }
    }

    // going down
    private void sink(int index) {
        while (2 * index <= size) {
            int j = 2 * index;
            // To make sure each time exchange the smallest item
            if (j+1 <= size && greater(pq.get(j),pq.get(j+1))) j++;
            if (!greater(pq.get(index),pq.get(j))) break;
            exch(index,j);
            index = j;
        }
    }

    //  P > Q
    private boolean greater(PriorityNode p, PriorityNode q) {
        return p.compareTo(q) > 0 ;
    }


    private void exch(int i, int j) {
        PriorityNode swap = pq.get(i);
        itemMapIndex.put(pq.get(j).getItem(),i);
        itemMapIndex.put(swap.getItem(),j);
        pq.set(i, pq.get(j));
        pq.set(j, swap);

    }



    @Override
    public int size() {
        return size;
    }

    private class PriorityNode implements Comparable<ArrayHeapMinPQ<T>.PriorityNode> {
        private T item;
        private double priority;

        private PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        private T getItem() {
            return item;
        }

        private double getPriority() {
            return priority;
        }

        private void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o.getClass() != this.getClass()) return false;
            if (o == this) return true;
            PriorityNode other = (PriorityNode) o;
            return other.item == this.item && other.priority == this.priority;
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public int compareTo(ArrayHeapMinPQ<T>.PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }
    }
}