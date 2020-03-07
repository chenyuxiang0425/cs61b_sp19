import org.junit.Test;
import static org.junit.Assert.*;

public class BubbleGridTest {

    @Test
    public void testBasic() {

        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0}};
        int[][] darts = {{1, 0}};
        int[] expected = {2};

        validate(grid, darts, expected);

        int[][] grid2 = {{1, 0, 1, 0},
                         {1, 0, 1, 1},
                         {0, 0, 0, 1}};
        int[][] darts2 = {{1, 0},{1,2}};
        int[] expected2 = {0,2};
        validate(grid2, darts2, expected2);

    }

    private void validate(int[][] grid, int[][] darts, int[] expected) {
        BubbleGrid sol = new BubbleGrid(grid);
        int[] acutal =  sol.popBubbles(darts);
        assertArrayEquals(expected,acutal);
    }
}
