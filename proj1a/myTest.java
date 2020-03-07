import org.junit.Test;
import static org.junit.Assert.*;

public class myTest {

    @Test
    public void testAddFirst() {
        LinkedListDeque List = new LinkedListDeque();
        int a = 10;
        int b = 15;
        int c = 20;
        List.addFirst(c);
        List.addFirst(b);
        List.addFirst(a);
        //expect 10 15 20
    }

    @Test
    public void testAddLast() {
        LinkedListDeque List = new LinkedListDeque();
        int a = 10;
        int b = 15;
        int c = 20;
        List.addLast(a);
        List.addLast(b);
        List.addLast(c);
        //expect 10 15 20
    }

    @Test
    public void testIsEmpty() {
        LinkedListDeque List = new LinkedListDeque();
        assertEquals(List.isEmpty(),true);
        int a = 5;
        List.addLast(a);
        assertEquals(List.isEmpty(),false);
    }

    @Test
    public void testPrintDeque() {
        LinkedListDeque List = new LinkedListDeque();
        int a = 10;
        int b = 15;
        int c = 20;
        List.addLast(a);
        List.addLast(b);
        List.addLast(c);
        //expect 10 15 20
        List.printDeque();

    }

    @Test
    public void testRemoveFirst() {
        LinkedListDeque List = new LinkedListDeque();
        int a = 10;
        int b = 15;
        int c = 20;
        List.addLast(a);
        List.addLast(b);
        List.addLast(c);
        //expect 10 15 20
        assertEquals(List.removeFirst(),a);
    }

    @Test
    public void testRemoveLast() {
        LinkedListDeque List = new LinkedListDeque();
        int a = 10;
        int b = 15;
        int c = 20;
        List.addLast(a);
        List.addLast(b);
        List.addLast(c);
        //expect 10 15 20
        assertEquals(List.removeLast(),c);
    }

    @Test
    public void testGetRecursion() {
        LinkedListDeque<String> List = new LinkedListDeque<>();
        String a = "a";
        String b = "b";
        String c = "c";
        List.addLast(a);
        List.addLast(b);
        List.addLast(c);
        //expect 10 15 20
        assertEquals(List.getRecursive(2),c);
        assertEquals(List.getRecursive(1),b);
    }

    @Test
    public void testInsert() {
        LinkedListDeque<String> List = new LinkedListDeque<>();
        List.addLast("a");
        List.addLast("b");
        List.addLast("c");
        List.insert("aaa",1);
    }

    @Test
    public void testreverse() {
        LinkedListDeque<String> List = new LinkedListDeque<>();
        List.addLast("a");
        List.addLast("b");
        List.addLast("c");
        //List.reverse();
    }
}
