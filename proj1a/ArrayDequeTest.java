import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    public static void testAddLast() {
        ArrayDeque a = new ArrayDeque();
        for (int i = 0; i <=13; i++) {
            a.addLast(i);
        }
    }

    public static void testPrintDeque() {
        ArrayDeque a = new ArrayDeque();
        for (int i = 0; i <=15; i++) {
            a.addLast(i);
        }
        for (int i = 0; i <=15; i++) {
            a.removeLast();
        }
        for (int i = 0; i <=14; i++) {
            a.addFirst(i);
        }
        a.printDeque();
    }

    public static void testRemoveLast() {
        ArrayDeque a = new ArrayDeque();
        for (int i = 0; i <=612; i++) {
            a.addLast(i);
        }
        for (int i = 0; i <=612; i++) {
            a.removeLast();
        }

    }


    public static void main(String[] args) {
        testAddLast();
        testPrintDeque();
        testRemoveLast();
    }
}

