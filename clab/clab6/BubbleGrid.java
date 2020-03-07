public class BubbleGrid {
    private int[][] grid;
    private int rowNum;
    private int colNum;
    private int dummyNode;
    /* Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space. */
    public BubbleGrid(int[][] grid) {
        this.grid = grid;
        rowNum = grid.length;
        colNum = grid[0].length;
        // DummyNode is used to connect with top elements (uf.union(node,dummyNode))
        // In this way, top elements can be checked with (uf.connected(node,dummyNode))
        dummyNode = rowNum * colNum;
    }

    /*
        Returns an array whose i-th element is the number of bubbles that fall after the i-th dart is thrown.
        Assume all elements of darts are unique, valid locations in the grid.
        Must be non-destructive and have no side-effects to grid.
    */
    public int[] popBubbles(int[][] darts) {
        int[] results = new int[darts.length];

        // to be non-destructive, make another int[][] to contain the same items of grid
        int[][] anothergrid = new int[rowNum][colNum];
        for (int a = 0; a < rowNum; a++) {
            for (int b = 0; b < colNum; b++) {
                anothergrid[a][b] = this.grid[a][b];
            }
        }

        // check the element
        for (int i = 0; i < darts.length; i++) {
            if (isEmpty(darts[i])) {
                results[i] = 0;
            } else {

                    // the number of 1 (before the shoot)
                    int NumberOfOneBefore = countNumberOfOne(anothergrid) - 1;
                    int[] dart = darts[i];
                    int y = dart[0];
                    int x = dart[1];
                    anothergrid[y][x] = 0;
                    // union the 1 and make the unsatisfied element (have no element in the first row) to be 0
                    doThingsOfUnion(anothergrid);
                    // count the number of 1
                    int NumberOfOneAfter = countNumberOfOne(anothergrid);
                    int PopDown = (NumberOfOneBefore - NumberOfOneAfter);
                    results[i] = PopDown;
                }
            }
        return results;
    }


    private boolean isEmpty(int[] dart) {
        int x = dart[0];
        int y = dart[1];
        return (grid[x][y] == 0);
    }


    private void doThingsOfUnion(int[][] grid) {
        UnionFind uf = new UnionFind(rowNum * colNum + 1);

        for (int y = 0; y < rowNum; y++) {
            for (int x = 0; x < colNum; x++) {

                if (grid[y][x] == 1) {
                    // if the first row has 1, then connect to dummyNode
                    if (y == 0) {
                        uf.union(node(y, x), dummyNode);
                    } else {
                        // up down left right
                        if (y - 1 >= 0 && grid[y - 1][x] == 1) {
                            uf.union(node(y,x),node(y - 1,x));
                        }
                        if (y + 1 < rowNum && grid[y + 1][x] == 1) {
                            uf.union(node(y,x),node(y + 1,x));
                        }
                        if (x - 1 >= 0 && grid[y][x - 1] == 1) {
                            uf.union(node(y,x),node(y,x - 1));
                        }
                        if (x + 1 < colNum && grid[y][x + 1] == 1) {
                            uf.union(node(y,x),node(y,x + 1));
                        }
                    }
                }
            }
        }
        // make unsatisfied elements to be 0
        for (int y = 0; y < rowNum; y++) {
            for (int x = 0; x < colNum; x++) {
                if (uf.connected(node(y,x),dummyNode)) {
                    grid[y][x] = 1;
                } else {
                    grid[y][x] = 0;
                }
            }
        }

    }

    // count the numbers of 1
    private int countNumberOfOne(int[][] darts) {
        int count = 0;
        for (int y = 0; y < rowNum; y++) {
            for (int x = 0; x < colNum; x++) {
                if (darts[y][x] == 1) {
                    count += 1;
                }
            }
        }
        return count;
    }


    // make int[][] fill in int[] of data structure 'UnionFind'
    private int node(int y,int x) {
        return y * colNum + x;
    }
}
