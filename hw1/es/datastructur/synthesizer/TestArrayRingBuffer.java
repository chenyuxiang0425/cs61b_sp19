package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {


    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        try {
            arb.dequeue();
        } catch (RuntimeException e) {
            System.out.println("The Array is empty, arb.dequeue() throw the exception is: " + e.toString());
        }

        for (int i = 0; i< arb.capacity(); i++) {
            arb.enqueue(i);
        }
        assertTrue(arb.isFull());
        int expected1 = 0;
        int actual1 = arb.dequeue();
        assertEquals(expected1,actual1);

        assertEquals((Integer) 1, arb.dequeue());

        assertFalse(arb.isFull());

        assertEquals((Integer) 2, arb.peek());

        arb.enqueue(11);
        arb.enqueue(12);
        try {
            arb.enqueue(13);
        } catch (RuntimeException e) {
            System.out.println("The Array is full, arb.enqueue(13) throw the exception is: " + e.toString());
        }
        assertTrue(arb.isFull());
        assertEquals((Integer) 2, arb.dequeue());

    }

    @Test
    public void TestEqual() {
        ArrayRingBuffer<Integer> arb1 = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> arb2 = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> arb3 = new ArrayRingBuffer(10);

        arb1.enqueue(5);
        arb2.enqueue(5);
        arb3.enqueue(5);

        arb1.enqueue(10);
        arb2.enqueue(10);
        arb3.enqueue(10);

        arb3.dequeue();

        assertTrue(arb1.equals(arb2));
        assertFalse(arb1.equals(arb3));
        assertTrue(arb1.equals(arb1));


    }
}
