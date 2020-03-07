import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayTest {

    @Test
    public void testInsert() {
        int[] a = new int[] {1,2,3,4,5};
        int[] actual = Array.insert(a,11,2);
        int[] expect = new int[] {1,2,11,3,4,5};
        assertArrayEquals(expect,actual);
    }

    @Test
    public void testReverse() {
        int[] a = new int[] {1,2,3,4,5};
        int[] expect = new int[] {5,4,3,2,1};
        Array.reverse(a);
        assertArrayEquals(expect,a);
    }

    @Test
    public void testReplicate() {
        int[] a = new int[] {1,2,3};
        int[] expect = new int[] {1,2,2,3,3,3};
        int[] actual = Array.replicate(a);
        assertArrayEquals(expect,actual);
    }

}
