package task.jbn;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestWrongArgs {

    @Test
    public void test_NullArguments () {
        Exception exception = assertThrows (IllegalArgumentException.class, () ->
            new RandomGen(null, null)
        );
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.NUMS_IS_NULL));
        assertTrue (errMessage.contains (RandomGen.PROBS_IS_NULL));
    }
    @Test
    public void test_EmptyArrays () {
        Exception exception;
        exception = Assertions.<IllegalArgumentException>assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {}, new float[] {});
        });
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.EMPTY_NUMS));
        assertTrue (errMessage.contains (RandomGen.EMPTY_PROBS));
    }
    @Test
    public void test_NullAndEmptyArrays () {
        Exception exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (null, new float[] {});
        });
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.NUMS_IS_NULL));
        assertTrue (errMessage.contains (RandomGen.EMPTY_PROBS));

        exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {}, null);
        });
        errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.EMPTY_NUMS));
        assertTrue (errMessage.contains (RandomGen.PROBS_IS_NULL));

    }

    @Test
    public void test_DuplicatedNums () {
        Exception exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {2, 3, 4, 2, 5}, new float[] {0.2f, 0.2f, 0.2f, 0.2f, 0.2f});
        });
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.DUPLICATED_NUM));
    }

    @Test
    public void test_NegativeProbabilities () {
        Exception exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {2, 3, 4, 2, 5}, new float[] {0.5f, -0.1f, 0.2f, 0.2f, 0.2f});
        });
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.NEGATIVE_PROBS));
    }

    @Test
    public void test_NotNormalizedProbabilities () {
        // Less than 1.0 - epsilon
        Exception exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {1, 2, 3, 4, 5}, new float[] {0.1999f, 0.2f, 0.2f, 0.2f, 0.2f});
        });
        String errMessage = exception.getMessage();
        assertTrue (errMessage.contains (RandomGen.PROBS_NOT_NORMALIZED));

        // Greater than 1.0 + epsilon
        exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {1, 2, 3, 4, 5}, new float[] {0.2001f, 0.2f, 0.2f, 0.2f, 0.2f});
        });
        errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.PROBS_NOT_NORMALIZED));
    }

    @Test
    public void test_DifferentArrayLengths () {
        Exception exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {1, 2, 3, 4}, new float[] {0.2f, 0.2f, 0.2f, 0.2f, 0.2f});
        });
        String errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.DIFFERENT_ARRAY_LENGTHS));

        exception = assertThrows (IllegalArgumentException.class, () -> {
            new RandomGen (new int[] {1, 2, 3, 4, 5, 6}, new float[] {0.2f, 0.2f, 0.2f, 0.2f, 0.2f});
        });
        errMessage = exception.getMessage();

        assertTrue (errMessage.contains (RandomGen.DIFFERENT_ARRAY_LENGTHS));
    }
}
