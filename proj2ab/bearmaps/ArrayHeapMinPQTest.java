package bearmaps;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {

    @Test
    public void BasicTest() {
        ArrayHeapMinPQ<String> arrayHeapMinPQ = new ArrayHeapMinPQ<>();
        assertEquals(0, arrayHeapMinPQ.size());
    }

    // assume add && removeSmall %% size done
    @Test
    public void BasicAddAndRemove() {
        ArrayHeapMinPQ<String> arrayHeapMinPQ = new ArrayHeapMinPQ<>();
        arrayHeapMinPQ.add("aaa",1.0);
        arrayHeapMinPQ.add("bbb",1.2);
        arrayHeapMinPQ.add("ccc",1.3);
        assertEquals(3,arrayHeapMinPQ.size());
        assertEquals("aaa",arrayHeapMinPQ.getSmallest());
        String smallest = arrayHeapMinPQ.removeSmallest();
        assertEquals("aaa",smallest);
        assertEquals(2,arrayHeapMinPQ.size());
        assertEquals("bbb",arrayHeapMinPQ.getSmallest());

    }

    // assume add && changePriority done
    @Test
    public void TestChangePrivority() {
        ArrayHeapMinPQ<String> arrayHeapMinPQ = new ArrayHeapMinPQ<>();

        arrayHeapMinPQ.add("ddd",1.5);
        arrayHeapMinPQ.add("eee",1.6);
        arrayHeapMinPQ.add("fff",1.7);
        arrayHeapMinPQ.add("ggg",1.8);
        arrayHeapMinPQ.changePriority("eee",1.0);
        assertEquals("eee",arrayHeapMinPQ.getSmallest());
        arrayHeapMinPQ.removeSmallest();
        assertEquals("ddd",arrayHeapMinPQ.getSmallest());
        arrayHeapMinPQ.add("aaa",0.9);
        assertEquals("aaa",arrayHeapMinPQ.getSmallest());
    }

    // assume add && removeSmallest && contains done
    @Test
    public void TestContain() {
        ArrayHeapMinPQ<String> arrayHeapMinPQ = new ArrayHeapMinPQ<>();
        arrayHeapMinPQ.add("ddd",1.5);
        arrayHeapMinPQ.add("eee",1.6);
        arrayHeapMinPQ.add("fff",1.7);
        arrayHeapMinPQ.add("ggg",1.8);
        assertTrue(arrayHeapMinPQ.contains("ddd"));
        arrayHeapMinPQ.removeSmallest();
        assertFalse(arrayHeapMinPQ.contains("ddd"));
    }

    // assume add && getSmallest done
    @Test
    public void TestGetSmallest() {
        ArrayHeapMinPQ<String> arrayHeapMinPQ = new ArrayHeapMinPQ<>();
        arrayHeapMinPQ.add("eee",1.6);
        arrayHeapMinPQ.add("fff",1.7);
        arrayHeapMinPQ.add("ddd",1.5);
        arrayHeapMinPQ.add("ggg",1.8);
        assertEquals("ddd",arrayHeapMinPQ.getSmallest());
    }

    // Time test
    @Test
    public void TestTime() {
        long start1 = System.currentTimeMillis();
        ArrayHeapMinPQ<Integer> arrayHeapMinPQ = new ArrayHeapMinPQ<>();
        for (int i = 0; i < 20000; i += 1) {
            arrayHeapMinPQ.add(i,10000-i);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("After 20000 insert, ArrayHeapMinPQ time spends: " + (end1 - start1) + "ms.");

        long start2 = System.currentTimeMillis();
        NaiveMinPQ<Integer> naiveMinPQ = new NaiveMinPQ<>();
        for (int i = 0; i < 20000; i += 1) {
            naiveMinPQ.add(i,10000-i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("After 20000 insert, NaiveMinPQ time spends: " + (end2 - start2) + "ms.");


        long start3 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            arrayHeapMinPQ.contains(i);
        }
        long end3 = System.currentTimeMillis();
        System.out.println("After 20000 contains, ArrayHeapMinPQ time spends: " + (end3 - start3) + "ms.");

        long start4 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            naiveMinPQ.contains(i);
        }
        long end4 = System.currentTimeMillis();
        System.out.println("After 20000 contians, NaiveMinPQ time spends: " + (end4 - start4) + "ms.");


        long start5 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            arrayHeapMinPQ.changePriority(i,i+20000);
        }
        long end5 = System.currentTimeMillis();
        System.out.println("After 20000 changePriority, ArrayHeapMinPQ time spends: " + (end5 - start5) + "ms.");

        long start6 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            naiveMinPQ.changePriority(i,i+20000);
        }
        long end6 = System.currentTimeMillis();
        System.out.println("After 20000 changePriority, NaiveMinPQ time spends: " + (end6 - start6) + "ms.");


        long start7 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            arrayHeapMinPQ.removeSmallest();
        }
        long end7 = System.currentTimeMillis();
        System.out.println("After 20000 removeSmallest, ArrayHeapMinPQ time spends: " + (end7 - start7) + "ms.");

        long start8 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i += 1) {
            naiveMinPQ.removeSmallest();
        }
        long end8 = System.currentTimeMillis();
        System.out.println("After 20000 removeSmallest, NaiveMinPQ time spends: " + (end8 - start8) + "ms.");
    }

        public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(ArrayHeapMinPQTest.class);
    }
}
