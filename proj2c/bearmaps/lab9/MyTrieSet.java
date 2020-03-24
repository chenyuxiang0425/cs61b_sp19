package bearmaps.lab9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61B {
    private Node root;    // root of trie

    public MyTrieSet() {
        root = new Node('\0',false);
    }

    private static class Node {
        private boolean isKey;
        private Map<Character,Node> map;
        private char currChar;


        private Node(char c, boolean blue) {
            isKey = blue;
            map = new HashMap<Character, Node>();
            currChar = c;
        }
    }
    
    @Override
    public void clear() {
        root = new Node('\0',false);
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }
        return curr.isKey;
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) throw new IllegalArgumentException();
        List<String> res = new ArrayList<>();
        Node curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) return null;
            curr = curr.map.get(c);
        }
        for (Node child : curr.map.values()) {
            keysWithPrefixhelper(res,prefix,child);
        }
        return res;

    }

    private void keysWithPrefixhelper(List<String> res, String word, Node currNode) {
        if (currNode.isKey) {
            res.add(word + currNode.currChar);
        }
        for (Node child : currNode.map.values()) {
            if (child != null) keysWithPrefixhelper(res, word + currNode.currChar, child);
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
