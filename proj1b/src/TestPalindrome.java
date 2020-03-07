import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertFalse(palindrome.isPalindrome("accra"));
        assertFalse(palindrome.isPalindrome("aaaaab"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));

    }

    @Test
    public void testIsPalindromeRecursion() {
        assertFalse(palindrome.isPalindromeRecursion("accra"));
        assertFalse(palindrome.isPalindromeRecursion("aaaaaba"));
        assertTrue(palindrome.isPalindromeRecursion("racecar"));
        assertTrue(palindrome.isPalindromeRecursion("noon"));
        assertTrue(palindrome.isPalindromeRecursion("a"));
        assertTrue(palindrome.isPalindromeRecursion(""));
    }

    @Test
    public void testIsPalindromeRecursionCharacterComparator() {
        CharacterComparator cc = new OffByOne();
        assertFalse(palindrome.isPalindrome("cat",cc));
        assertFalse(palindrome.isPalindrome("aaaaab",cc));
        assertTrue(palindrome.isPalindrome("befgda",cc));
        assertTrue(palindrome.isPalindrome("acedb",cc));
        assertTrue(palindrome.isPalindrome("a",cc));
        assertTrue(palindrome.isPalindrome("",cc));
    }

    @Test
    public void testIsPalindromeRecursionCharacterComparatorByN() {
        CharacterComparator cc = new OffByN(5);
        assertFalse(palindrome.isPalindrome("fah",cc));
        assertFalse(palindrome.isPalindrome("aaaaaf",cc));
        assertTrue(palindrome.isPalindrome("faffa",cc));
        assertTrue(palindrome.isPalindrome("fafafa",cc));
        assertTrue(palindrome.isPalindrome("a",cc));
        assertTrue(palindrome.isPalindrome("",cc));
    }
}