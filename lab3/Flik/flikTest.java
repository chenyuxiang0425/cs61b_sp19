import static org.junit.Assert.*;
import org.junit.Test;
public class flikTest {

    @Test
    public void testFlik() {
        int a = 128;
        int b = 228;
        int c = 128;
        //assertFalse(Flik.isSameNumber(a,b));
        assertTrue(Flik.isSameNumber(a,c));
    }
}
