package task.jbn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class TestDistributions {
    private static boolean systemOutPrintEnabled = false;
    private static final Logger LOG = LoggerFactory.getLogger (TestDistributions.class);

    @BeforeAll
    public static void setPrint () {
        systemOutPrintEnabled = (System.getenv ("PRINT_ON_CONSOLE") != null);
    }

    private static void print (String s) {
        if (systemOutPrintEnabled) System.out.println (s);
        else LOG.info (() -> s);
    }

    final int[] vals = { 5, 40, 300, 2, -10};

    private void doCalculations (float[] probs) {
        int N = 10_000_000;

        try {
            RandomGen gen = new RandomGen (vals, probs);
            Map<Integer, Integer> counts = new HashMap<>();
            IntStream.of (vals).forEach (v -> counts.put (v, 0));

            for (int i = 0; i < N; i++) {
                int n = gen.nextNum ();
                int cnt = counts.get (n);
                counts.put (n, cnt + 1);
            }

            print("");
            counts.keySet().stream().sorted().forEach (n -> print (n + ": " + counts.get (n)));

            // Calculate chi-square (4 degrees of freedom, 0.05 significance, value = 9.488)
            double weight = 1.0 / N;
            double chi_sqr = 0.0;
            for (int i = 0; i < probs.length; i++) {
                if (probs[i] != 0.0) {
                    double d = weight * counts.get (vals[i]) - probs[i];
                    chi_sqr += d * d / probs[i];
                }
            }

            chi_sqr *= N;
            print ("Chi-2 = " + chi_sqr);

            assertTrue (chi_sqr <= 9.488);
        } catch (IllegalArgumentException e) {
            print (e.getMessage());
        }

        print ("\n----------------------\n");
    }

    @Test
    public void test_EqualDistribution() {
        print ("Equal distribution");
        doCalculations (new float[] { 0.2f, 0.2f, 0.2f, 0.2f, 0.2f });
    }

    @Test
    public void test_SingularDistribution() {
        print ("Singual distribution");
        doCalculations (new float[] { 0.01f, 0.01f, 0.01f, 0.96f, 0.01f });
    }

    @Test
    public void test_StepDistribution() {
        print ("Step distribution");
        doCalculations (new float[] { 0.25f, 0.125f, 0.125f, 0.25f, 0.25f });
    }

    @Test
    public void test_LinearDistribution() {
        print ("Linear distribution");
        doCalculations (new float[] { 0.2f, 0.25f, 0.3f, 0.15f, 0.1f });
    }
}
