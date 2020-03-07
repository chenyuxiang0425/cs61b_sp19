import java.util.Arrays;

public class UnionFind {
    private int[] intSet;
    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        intSet = new int[n];
        Arrays.fill(intSet, -1);
    }

    public UnionFind(int[][] grid) {

    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex < 0 || vertex >= intSet.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid index");
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        validate(v1);
        int root = find(v1);
        return -parent(root);
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        validate(v1);
        return intSet[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a
       vertex with itself or vertices that are already connected should not
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);
        int v1Size = sizeOf(v1);
        int v2Size = sizeOf(v2);
        int v1Root = find(v1);
        int v2Root = find(v2);
        if (connected(v1,v2)) {
            return;
        }

        if (v1Size > v2Size) {
            intSet[v1Root] -= sizeOf(v2Root);
            intSet[v2Root] = v1Root;
        } else {
            intSet[v2Root] -= sizeOf(v1Root);
            intSet[v1Root] = v2Root;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        validate(vertex);
        int root = vertex;
        // find the index of the root
        while (parent(root) >= 0) {
            root = parent(root);
        }

        // path-compression
        int currParent;
        while (vertex != root) {
            currParent = parent(vertex);
            intSet[vertex] = root;
            vertex = currParent;
        }
        return root;
    }

}

