import org.junit.Test;
import static org.junit.Assert.*;

public class RabinKarpAlgorithmTests {
    @Test
    public void basic() {
        String input1 = "hello";
        String pattern1 = "ell";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input1, pattern1));

        String input2 = "hello";
        String pattern2 = "ello";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input2, pattern2));

        String input3 = "helloaa";
        String pattern3 = "ellaa";
        assertEquals(-1, RabinKarpAlgorithm.rabinKarp(input3, pattern3));

        String input4 = "hello";
        String pattern4 = "ecl";
        assertEquals(-1, RabinKarpAlgorithm.rabinKarp(input4, pattern4));

        String input5 = "aaaab";
        String pattern5 = "ab";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input5, pattern5));

        String input6 = "rwqefw";
        String pattern6 = "qef";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input6, pattern6));





    }
}
