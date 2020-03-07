import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class blackWhiteChessTest {

    @Test
    public void basicTest() {
        int[][] grid = {{0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 2, 0},
                        {0, 0, 0, 1, 1, 1, 0, 0},
                        {0, 0, 2, 1, 2, 2, 0, 0},
                        {0, 0, 1, 1, 1, 2, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0}};

        int[][] expect = {{2,3},{3,3},{3,5},{6,2},{7,3},{7,4}};
        blackWhiteChess bwc = new blackWhiteChess(grid);
        List actual = bwc.check();

        //assertArrayEquals(expect,actual);
    }

    @Test
    public void ifNearOtherChessTest() {
        int[][] grid = {{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 2, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 2, 1, 2, 2, 0, 0},
                {0, 0, 1, 1, 1, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        blackWhiteChess bwc = new blackWhiteChess(grid);
        int[] WhiteChess = new int[]{0,2};
        assertFalse(bwc.ifNearOtherChess(WhiteChess));
        int[] WhiteChess1 = new int[]{2,3};
        int[] WhiteChess2 = new int[]{3,3};
        int[] WhiteChess3 = new int[]{7,5};
        int[] WhiteChess4 = new int[]{6,4};
        assertTrue(bwc.ifNearOtherChess(WhiteChess1));
        assertTrue(bwc.ifNearOtherChess(WhiteChess2));
        assertFalse(bwc.ifNearOtherChess(WhiteChess3));
        assertTrue(bwc.ifNearOtherChess(WhiteChess4));

    }

    @Test
    public void whetherOtherSideTest() {
        int[][] grid = {{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 2, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 2, 1, 2, 2, 0, 0},
                {0, 0, 1, 1, 1, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        blackWhiteChess bwc = new blackWhiteChess(grid);
        int[] checkpoint1 = new int[]{3,1};
        assertTrue(bwc.whetherOtherSide(checkpoint1));
        int[] checkpoint2 = new int[]{6,4};
        int[] checkpoint3 = new int[]{6,2};
        int[] checkpoint4 = new int[]{6,5};
        assertTrue(bwc.whetherOtherSide(checkpoint2));
        assertTrue(bwc.whetherOtherSide(checkpoint3));
        assertTrue(bwc.whetherOtherSide(checkpoint4));
        int[] checkpoint5 = new int[]{6,1};
        int[] checkpoint6 = new int[]{3,6};
        int[] checkpoint7 = new int[]{3,4};
        assertFalse(bwc.whetherOtherSide(checkpoint5));
        assertTrue(bwc.whetherOtherSide(checkpoint6));
        assertFalse(bwc.whetherOtherSide(checkpoint7));


    }

}
