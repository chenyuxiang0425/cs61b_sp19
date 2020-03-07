public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        char[] a = word.toCharArray();
        Deque<Character> wordtoDeque = new ArrayDeque<>();
        for (char w:a) {
            wordtoDeque.addLast(w);
        }
        return wordtoDeque;
    }

    //isPalindrome循环
    public boolean isPalindrome(String word) {
        Deque<Character> dequeWord = wordToDeque(word);
        if (dequeWord.size() % 2 == 0) {
            int size = dequeWord.size();
            for (int i=0; i<size/2; i+=1) {
                char first = dequeWord.removeFirst();
                char last = dequeWord.removeLast();
                if (first != last) {
                    return false;
                }
            }
            return true;
        } else {
            int size = dequeWord.size();
            for (int i=0; i<(size-1)/2; i+=1) {
                char first = dequeWord.removeFirst();
                char last = dequeWord.removeLast();
                if (first != last) {
                    return false;
                }
            }
            return true;
        }
    }
    //isPalindrome递归
    public boolean isPalindromeRecursion(String word) {
        Deque<Character> dequeWord = wordToDeque(word);
        return isPalindromeRecursionHelper(dequeWord);
    }

    private boolean isPalindromeRecursionHelper(Deque<Character> dequeWord) {
        if (dequeWord.size() <= 1 ){
            return true;
        }
        char first = dequeWord.removeFirst();
        char last = dequeWord.removeLast();
        if (first == last) {
           return isPalindromeRecursionHelper(dequeWord);
        } else {
            return false;
        }
    }


    //Overload isPalindrome
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> dequeWord = wordToDeque(word);
        return isPalindromeRecursionHelper(dequeWord, cc);
    }

    private boolean isPalindromeRecursionHelper(Deque<Character> dequeWord, CharacterComparator cc) {

        if (dequeWord.size() <= 1 ){
            return true;
        }
        char first = dequeWord.removeFirst();
        char last = dequeWord.removeLast();
        if (cc.equalChars(first,last)) {
            return isPalindromeRecursionHelper(dequeWord,cc);
        } else {
            return false;
        }
    }


}
