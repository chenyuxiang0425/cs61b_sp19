public class IntList {

    private class IntNode {
        IntNode next;
        int item;
        public IntNode(int item,IntNode next) {
            this.item = item;
            this.next = next;
        }
    }
    int size;
    IntNode sentinel;

    public IntList() {
        sentinel = new IntNode(120,null);
        size = 0;
    }

    public void addLast(int x) {
        IntNode p = sentinel;
        for (int i = 0; i<size ;i ++) {
            p = p.next;
        }
        p.next = new IntNode(x,null);
        size += 1;
    }

    public void reverse() {
        sentinel.next=reversehelp(sentinel.next);
    }

    public IntNode reversehelp(IntNode head) {
        if (head.next == null) {
            return head;
        }
        IntNode last = reversehelp(head.next);
        head.next.next = head;
        head.next = null;


        return last;
    }

    public static void main(String[] args) {
        IntList L1 = new IntList();
        L1.addLast(5);
        L1.addLast(10);
        L1.addLast(15);
        L1.addLast(20);
        L1.addLast(25);
        L1.reverse();
    }
}
