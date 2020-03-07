import java.util.Random;

/**
 * Created by hug.
 */
public class ExperimentHelper {

    /**
     * Returns the internal path length for an optimum binary search tree of
     * size N.
     */
    public static int optimalIPL(int N) {
        int depth = 0;
        int tmp = N;
        int res = 0;
        int numCount = 0;

        while (tmp != 1) {
            depth = depth + 1;
            tmp = tmp / 2;
        }
        //count the  “Internal Path Length” of a BST of depth - 1
        for (int i = 0; i < depth; i++) {
            int iIPL = (int) Math.pow(2, i) * i;
            int iNum = (int) Math.pow(2, i);
            res = res + iIPL;
            numCount = numCount + iNum;
        }
        //count the res
        int restNums = N - numCount;
        res = res + restNums * depth;
        return res;
    }

    /**
     * Returns the average depth for nodes in an optimal BST of
     * size N.
     */
    public static double optimalAverageDepth(int N) {
        return (double) optimalIPL(N) / N;
    }


    /**
     * Delete one item and insert one item
     * * @param bst BST tree
     */
    public static void insertAndDeleteSuccessor(BST<Integer> bst) {
        Random r = new Random();
        int length = bst.size();

        // delete an item
        while (length == bst.size()) {
            int item = r.nextInt(5000);
            if (bst.contains(item)){
                bst.deleteTakingSuccessor(item);
            }
        }

        // insert an item
        length = bst.size(); // size has changed
        while (length == bst.size()) {
            int item = r.nextInt(5000);
            if (!bst.contains(item)){
                bst.add(item);
            }
        }
    }

    public static void insertAndDeleteRandom(BST<Integer> bst) {

        Random r = new Random();
        int length = bst.size();

        // delete an item
        while (length == bst.size()) {
            int item = r.nextInt(5000);
            if (bst.contains(item)){
                bst.deleteTakingRandom(item);
            }
        }

        // insert an item
        length = bst.size(); // size has changed
        while (length == bst.size()) {
            int item = r.nextInt(5000);
            if (!bst.contains(item)){
                bst.add(item);
            }
        }
    }


}