package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private int numOfOpen;
    private WeightedQuickUnionUF uf;
    private int virtual_top;
    private int virtual_bottom;


    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IndexOutOfBoundsException();
        }
        size = N;
        grid = new boolean[N][N]; // all the items is false
        numOfOpen = 0;
        uf = new WeightedQuickUnionUF(N * N + 2);
        virtual_top = N * N;
        virtual_bottom = N * N + 1;
    }

    private int transform(int row, int col) {
        return row * size + col;
    }

    private void validate(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException();
        }

    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row,col);
        if (isOpen(row,col)) {
            return;
        }
        grid[row][col] = true;
        numOfOpen ++;

        if (row == 0) {
            uf.union(transform(row,col),virtual_top);
        }

        if (row == size - 1) {
            uf.union(transform(row,col),virtual_bottom);
        }

        // up
        if (row - 1 >= 0 && isOpen(row - 1,col)) {
            uf.union(transform(row,col),transform(row - 1, col));
        }
        // down
        if (row + 1 < size && isOpen(row + 1,  col)) {
            uf.union(transform(row,col),transform(row + 1, col));
        }
        // left
        if (col -1 >= 0 && isOpen(row, col - 1)) {
            uf.union(transform(row,col),transform(row, col - 1));
        }
        // right
        if (col +1 < size && isOpen(row, col + 1)) {
            uf.union(transform(row,col),transform(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)  {
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row,col);
        return uf.connected(transform(row,col),virtual_top);
    }

    // number of open sites
    public int numberOfOpenSites(){
        return numOfOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(virtual_top,virtual_bottom);
    }

    public static void main(String[] args) {}
}
