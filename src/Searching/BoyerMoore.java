/**
 * Two scientists, Boyer and Moore, came up with another idea. Why not
 * compare the pattern to the text from right to left instead of left
 * to right, while keeping the shift direction the same.
 * As expected, this will run in O(m * n) time. But this algorithm led
 * to the implementation of occurrence and the match heuristics which
 * speeds up the algorithm significantly.
 * Space usage doesn't depend on the size of
 * the pattern, but only on the size of the alphabet which is 256 since that is
 * the maximum value of ASCII character in English alphabet.
 */

package Searching;

public class BoyerMoore {
    public static int BoyerMooreHorspoolSimpleSearch(String pattern, String text) {
        int patternSize = pattern.length();
        int textSize = text.length();

        int i = 0, j = 0;

        while ((i + patternSize) <= textSize) {
            j = patternSize - 1;
            while (text.charAt(i + j) == pattern.charAt(j)) {
                j--;
                if (j < 0)
                    return i;
            }
            i++;
        }
        return -1;
    }

    public static int BoyerMooreHorspoolSearch(String pattern, String text) {

        int shift[] = new int[256];

        for (int k = 0; k < 256; k++) {
            shift[k] = pattern.length();
        }

        for (int k = 0; k < pattern.length() - 1; k++) {
            shift[pattern.charAt(k)] = pattern.length() - 1 - k;
        }

        int i = 0, j = 0;

        while ((i + pattern.length()) <= text.length()) {
            j = pattern.length() - 1;

            while (text.charAt(i + j) == pattern.charAt(j)) {
                j -= 1;
                if (j < 0)
                    return i;
            }

            i = i + shift[text.charAt(i + pattern.length() - 1)];
        }
        return -1;
    }
}
