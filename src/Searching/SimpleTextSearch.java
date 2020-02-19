/**
 * The idea of this algorithm is straightforward: iterate through the text
 * and if there is a match for the first letter of the pattern, check if all
 * the letters of the pattern match the text.
 * If m is a number of the letters in the pattern, and n is the number of the
 * letters in the text, time complexity of this algorithms is O(m(n-m + 1)).
 */

package Searching;

public class SimpleTextSearch {
    public static int simpleTextSearch(String pattern, String text) {
        int patternSize = pattern.length();
        int i = 0;
        int textSize = text.length();
        while ((i + patternSize) <= textSize) {
            int j = 0;
            while (text.charAt(i + j) == pattern.charAt(j)) {
                j += 1;
                if (j >= patternSize) {
                    return i;
                }
            }
            i += 1;
        }
        return -1;
    }
}
