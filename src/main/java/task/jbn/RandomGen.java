package task.jbn;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomGen {
    public static final String NUMS_IS_NULL = "Argument \"randomNums\" is null\n";
    public static final String EMPTY_NUMS = "Argument \"randomNums\" is empty\n";
    public static final String DUPLICATED_NUM = "There are duplicated values in argument \"randomNums\"\n";
    public static final String PROBS_IS_NULL = "Argument \"probabilities\" is null\n";
    public static final String EMPTY_PROBS = "Argument \"probabilities\" is empty\n";
    public static final String PROBS_NOT_NORMALIZED = "Argument \"probabilities\" is not normalized (the sum of probabilities must be 1)\n";
    public static final String NEGATIVE_PROBS = "Argument \"probabilities\" contains negative values\n";
    public static final String DIFFERENT_ARRAY_LENGTHS = "Arguments \"randomNums\" and \"probabilities\" have different lengths\n";

    private static final float epsilon = 1.0e-6f;  // precision of probabilities normalization

    private final int [] randomNums;
    private final float [] probabilities;

    private float [] aggregates = null;
    private Random randomGen = null;

    public RandomGen (int[] randomNums, float[] probabilities) {
        checkArguments (randomNums, probabilities);

        this.randomNums = randomNums;
        this.probabilities = probabilities;
    }

    public int nextNum() {
        // Lazy initialization of accumulated probabilities
        if (this.aggregates == null) {
            this.initRandomGenerator();
            this.initAggregates();
        }

        float rnd = this.randomGen.nextFloat();
        int index = this.findIndex (rnd);
        return this.randomNums [index];
    }

    private int findIndex (float val) {
        int idx = Arrays.binarySearch (aggregates, val);
        if (idx < 0) {
            idx = - idx - 1;
        }

        return idx;
    }

    private void initRandomGenerator() {
        this.randomGen = new Random();
    }

    private void initAggregates() {
        int len = this.probabilities.length;
        this.aggregates = new float [len];
        this.aggregates[0] = this.probabilities[0];
        for (int i = 1; i < len - 1; i++) {
            this.aggregates[i] = this.aggregates[i-1] + this.probabilities [i];
        }
        this.aggregates[len-1] = 1.0f;  //  to avoid floating point rounding problems
    }

    private void checkArguments (int[] randomNums, float[] probabilities) throws IllegalArgumentException {
        StringBuilder msg = new StringBuilder();

        if (randomNums == null) {
            msg.append (NUMS_IS_NULL);

        } else if (randomNums.length == 0) {
            msg.append (EMPTY_NUMS);

        } else if (randomNums.length != IntStream.of (randomNums).distinct ().count ()) {
            msg.append (DUPLICATED_NUM);
        }

        if (probabilities == null) {
            msg.append (PROBS_IS_NULL);

        } else if (probabilities.length == 0) {
            msg.append (EMPTY_PROBS);

        } else {  // are probabilities normalized / positive
            float acc = 1.0f;
            for (float probability : probabilities) {
                if (probability < 0.0f) {
                    msg.append(NEGATIVE_PROBS);
                    break;
                }
                acc -= probability;
            }

            if (Math.abs (acc) > epsilon) {
                msg.append (PROBS_NOT_NORMALIZED);
            }
        }

        if ((randomNums != null) && (probabilities != null) &&
                randomNums.length != probabilities.length) {
            msg.append (DIFFERENT_ARRAY_LENGTHS);
        }

        if (msg.length () > 0) {
            throw new IllegalArgumentException (msg.toString());
        }
    }
}
