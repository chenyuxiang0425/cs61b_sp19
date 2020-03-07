import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V >{
    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private int size;


        private Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    public BSTMap() {
    }


    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        //location the place??
        Node T = get(root, key);
        if (T == null) {
            return null;
        }
        return T.value;
    }

    private Node get(Node T,K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        if (T == null) {
            return null;
        } else if (key.compareTo(T.key) == 0) {
            return T;
        } else if (key.compareTo(T.key) > 0) { //key > T
            return get(T.right, key);
        } else {
            return get(T.left, key);
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node T) {
        if (T == null) {
            return 0;
        }
        return T.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        root = put(root,key,value);
    }

    private Node put(Node T, K key, V value) {
        if (T == null) {
            return new Node(key, value, 1);
        } else if (key.compareTo(T.key) < 0) { // this < Node
            T.left = put(T.left,key,value);
        } else {
            T.right = put(T.right,key,value);
        }
        T.size = size(T.left) + 1 + size(T.right);
        return T;
    }

    @Override
    public Set<K> keySet() {
        Set<K> BSTSet = new HashSet<>();
        for (int i= 0;i<size();i++) {
            Node n = select(i);
            BSTSet.add(n.key);
        }
        return BSTSet;
    }

    @Override
    public V remove(K key) {
        // 1.如果无叶节点： 直接remove
        // 2.如果有一个叶子结点： remove后将叶子节点接上
        // 3.如果有两个叶子节点： remove后，将其分为两棵树
        //   任取前树的最右 或 后树的最左
        if (!containsKey(key)) {
            return null;
        }
        V toRemove = get(key);
        root = remove(root,key);
        return toRemove;
    }



    @Override
    public V remove(K key, V value) {

        if (!containsKey(key)) {
            return null;
        }
        if (!get(key).equals(value)) {
            return null;
        }
        root = remove(root,key);
        return value;
    }


    /*
    answer: https://algs4.cs.princeton.edu/32bst/
     1 Save a link to the node to be deleted in T
       保存要删除的节点的链接
     2 Set X to point to its successor min(T.right).
       设置 X 指向它的继任者 min(t.right)
     3 Set the right link of X (which is supposed to point to the BST containing all the keys larger than X.key) to deleteMin(t.right),
       the link to the BST containing all the keys that are larger than x.key after the deletion.
       将 X 的右侧链接（应该指向包含所有大于X.key的键的BST）设置为deleteMin（t.right），
       之后指向包含所有大于 X.key 的键的BST的链接 删除
     4 Set the left link of X (which was null) to T.left (all the keys that are less than both the deleted key and its successor).
       将X的左链接（为空）设置为T.left（所有小于已删除键及其后继键的键）

     */
    private Node remove(Node T, K key) {
        if (T == null) {
            return null;
        }
        int cmp = key.compareTo(T.key);
        if (cmp < 0) {
            T.left = remove(T.left, key);  // 左子树递归删除
        } else if (cmp > 0) {
            T.right = remove(T.right, key);  // 右子树递归删除
        } else { // find the key
            if (T.right == null) { // 把无叶子节点和单个叶子节点都处理了
                return T.left;     // return 的是 T 的下 1 个节点
            }
            if (T.left == null) {
                return T.right;
            }
            // 左右两边都不空：
            //step 1 :link to the node to be deleted .
            Node tmp = T;
            T = min(T.right); // Now T has been located in successor
            T.right = deleteMin(tmp.right);
            T.left = tmp.left;
        }
        T.size = 1 + size(T.right) + size(T.left);
        return T;
    }

    private Node min(Node T) {
        if (T.left == null) {
            return T;
        }
        return min(T.left);
    }

    private Node deleteMin(Node T) {

        if (T.left == null) {
            return T.right;
        }
        T.left = deleteMin(T.left);
        T.size = size(T.left) + size(T.right) + 1;
        return T;
    }


    public void printInOrder(){
        //TODO :prints out your BSTMap in order of increasing Key
        // 遍历
        for (int i = 0; i < size(); i++) {
            Node n = select(i);
            System.out.println(n.key + " " +n.value);
        }
    }

    private Node select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException();
        }
        return select(root,k);
    }


    //Inorder Traversal
    // 节点 T 的左子树个数 leftNodes，与搜寻的 k 的大小
    // 进行比较，如果 k 小就去左子树找，如果 k 大就去右子树找
    private Node select(Node T,int k) {
        if (T == null) {
            return null;
        }
        // 记录当前节点左子树的 size
        int leftNodes = size(T.left);
        if (leftNodes > k) {  // k 小
            return select(T.left, k);
        } else if (leftNodes < k) { // k 大
            // 右子树规模是 k - leftNodes - 1
            //     T
            //   a    c    --------       c
            // b  e  f  d               f   d
            return select(T.right, k - leftNodes - 1);
        } else {
            // 刚好找到
            return T;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    public class BSTMapIterator implements Iterator<K> {
        private int cursor;

        public BSTMapIterator() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public K next() {
            Node n = select(cursor);
            cursor += 1;
            return n.key;
        }
    }
}

