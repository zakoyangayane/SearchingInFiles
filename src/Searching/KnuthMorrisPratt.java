/**
 * The idea of the Knuth-Morris-Pratt algorithm is the calculation of
 * shift table which provides us with the information where we should
 * search for our pattern candidates.
 * The time complexity of this algorithm is O(m+n).
 */

package Searching;

public class KnuthMorrisPratt {
    public static int KnuthMorrisPrattSearch(String pattern, String text) {
        int patternSize = pattern.length();
        int textSize = text.length();
        int i = 0, j = 0;
        int[] shift = KnuthMorrisPrattShift(pattern);

        while ((i + patternSize) <= textSize) {
            while (text.charAt(i + j) == pattern.charAt(j)) {
                j += 1;
                if (j >= patternSize)
                    return i;
            }

            if (j > 0) {
                i += shift[j - 1];
                j = Math.max(j - shift[j - 1], 0);
            } else {
                i++;
                j = 0;
            }
        }
        return -1;
    }

    public static int[] KnuthMorrisPrattShift(String pattern) {
        int patternSize = pattern.length();

        int[] shift = new int[patternSize];
        shift[0] = 1;

        int i = 1, j = 0;

        while ((i + j) < patternSize) {
            if (pattern.charAt(i + j) == pattern.charAt(j)) {
                shift[i + j] = i;
                j++;
            } else {
                if (j == 0)
                    shift[i] = i + 1;

                if (j > 0) {
                    i = i + shift[j - 1];
                    j = Math.max(j - shift[j - 1], 0);
                } else {
                    i = i + 1;
                    j = 0;
                }
            }
        }
        return shift;
    }
}
