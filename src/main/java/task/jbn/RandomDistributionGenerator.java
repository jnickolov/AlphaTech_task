package task.jbn;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * A helper class to generate a whole distribution.
 *   @param values: possible output values
 *   @param probabilities: their respective probabilities
 *   @param count: total number of samples in the distribution
 *
 * Example usage:
 *             int[] vals = new int[] {1,2, 3, 4, 5};
 *             float[] probs = new float[] {0.1, 0.2, 0.3, 0.3, 0.1};
 *             int cnt = 100_000_000;
 *             Map<Integer,Integer> result = RandomDistributionGenerator (vals, probs, cnt);
 */
public class RandomDistributionGenerator {
    public static Map<Integer, Integer> generateDistribution (
            int[] values, float[] probabilities, int count) {

        if (count <= 0) {
            throw new IllegalArgumentException ("Argument \"count\" must be positive\n");
        }

        final RandomGen gen = new RandomGen (values, probabilities);  // could throw IllegaArgumentException

        final Map<Integer, Integer> histogram = new HashMap<>();
        IntStream.of (values).forEach (v -> histogram.put (v, 0));

        IntStream.range (0, count).forEach (i -> {
            int n = gen.nextNum ();
            int cnt = histogram.get (n);
            histogram.put (n, cnt + 1);
        });

        return histogram;
    }
}