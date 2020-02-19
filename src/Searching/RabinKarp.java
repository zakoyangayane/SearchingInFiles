/**
 * The idea of Rabin Karp algorithm is to use hashing to find a pattern
 * in a text. At the beginning of the algorithm, we need to calculate a
 * hash of the pattern which is later used in the algorithm. This process
 * is called fingerprint calculation.
 * The important thing about pre-processing step is that its time complexity
 * is O(m) and iteration through text will take O(n) which gives time complexity
 * of whole algorithm O(m+n).
 * In worst-case scenario, time complexity for this algorithm is O(m(n-m+1)).
 * However, on average this algorithm has O(n+m) time complexity.
 */

package Searching;

import java.math.BigInteger;
import java.util.Random;


public class RabinKarp {
    public static int RabinKarpMethod(String pattern, String text) {
        int patternSize = pattern.length();
        int textSize = text.length();
        long prime = getBiggerPrime(patternSize);

        long r = 1;
        for (int i = 0; i < patternSize - 1; i++) {
            r *= 2;
            r = r % prime;
        }

        long[] t = new long[textSize];

        long pfinger = 0;

        for (int j = 0; j < patternSize; j++) {
            if (textSize != 0) {
                t[0] = (2 * t[0] + text.charAt(j)) % prime;
                pfinger = (2 * pfinger + pattern.charAt(j)) % prime;
            }
        }

        int i = 0;
        boolean passed = false;

        int diff = textSize - patternSize;
        for (i = 0; i <= diff; i++) {
            if (t[i] == pfinger) {
                passed = true;
                for (int k = 0; k < patternSize; k++) {
                    if (text.charAt(i + k) != pattern.charAt(k)) {
                        passed = false;
                        break;
                    }
                }

                if (passed) {
                    return i;
                }
            }

            if (i < diff) {
                long value = 2 * (t[i] - r * text.charAt(i)) + text.charAt(i + patternSize);
                t[i + 1] = ((value % prime) + prime) % prime;
            }
        }
        return -1;
    }

    public static long getBiggerPrime(int m) {
        BigInteger prime = BigInteger.probablePrime(getNumberOfBits(m) + 1, new Random());
        return prime.longValue();
    }

    private static int getNumberOfBits(int number) {
        return Integer.SIZE - Integer.numberOfLeadingZeros(number);
    }
}
