package se2.alpha.riskappbackend;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se2.alpha.riskappbackend.model.db.Dice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
public class DiceTest {

    private Dice dice;

    @BeforeEach
    public void setUp() {
        dice = new Dice();
    }

    @Test
    public void testRoll() {
        IntStream.range(0, 1000).forEach(i -> {
            int result = dice.roll();
            assertTrue(result >= 1 && result <= dice.getNumSides(), "Roll should be between 1 and " + dice.getNumSides());
        });
    }

    @Test
    public void testRollMultipleTimesLength() {
        int numRolls = 10;
        int[] results = dice.rollMultipleTimes(numRolls);
        Assertions.assertEquals(numRolls, results.length, "Array length should match the number of rolls");
    }

    @Test
    public void testRollMultipleTimesValues() {
        int numRolls = 10;
        int[] results = dice.rollMultipleTimes(numRolls);
        for (int result : results) {
            assertTrue(result >= 1 && result <=dice.getNumSides(), "Each roll should be between 1 and " + dice.getNumSides());
        }
    }

    @Test
    public void testRollDistribution() {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        int totalRolls = 6000; // We choose a large number for a better distribution check
        for (int i = 0; i < totalRolls; i++) {
            int result = dice.roll();
            frequencyMap.put(result, frequencyMap.getOrDefault(result, 0) + 1);
        }
        // Expecting roughly even distribution, we should have about totalRolls / NUM_SIDES per side
        double expectedFrequencyPerSide = (double) totalRolls / dice.getNumSides();
        double allowedVariance = expectedFrequencyPerSide * 0.1; // Allowing 10% variance
        frequencyMap.values().forEach(frequency -> {
            assertTrue(
                    frequency > expectedFrequencyPerSide - allowedVariance &&
                            frequency < expectedFrequencyPerSide + allowedVariance,
                    "Frequency " + frequency + " of a roll is outside the expected range"
            );
        });
    }

    @Test
    public void testRollMultipleTimesNotNull() {
        int numRolls = 10;
        int[] results = dice.rollMultipleTimes(numRolls);
        Assertions.assertNotNull(results, "Result array should not be null");
        assertTrue(Arrays.stream(results).noneMatch(r -> r < 1 || r > dice.getNumSides()), "All roll results should be between 1 and " + dice.getNumSides());
    }


    @Test
    public void testRollMultipleTimesSum() {
        int numRolls = 100;
        int[] results = dice.rollMultipleTimes(numRolls);
        int sum = Arrays.stream(results).sum();
        int minSum = numRolls; // if all rolls are 1
        int maxSum = numRolls * dice.getNumSides(); // if all rolls are the maximum value
        assertTrue(sum >= minSum && sum <= maxSum, "The sum of all rolls should be within the expected range");
    }

    @Test
    public void testRepeatedRollsAreRandom() {
        int numRolls = 10;
        int[] firstResults = dice.rollMultipleTimes(numRolls);
        int[] secondResults = dice.rollMultipleTimes(numRolls);
        Assertions.assertFalse(Arrays.equals(firstResults, secondResults), "It is unlikely (though not impossible) for two sequences of random rolls to be identical");
    }

    @Test
    public void testIsResultSorted() {
        boolean isSorted = true;
        int lastNumber = 0;
        Integer[] rolls = Dice.rollMultipleTimes((Integer) 10);
        for(int i = rolls.length - 1; i >= 0; i--)
        {
            if(lastNumber > rolls[i])
                isSorted = false;
        }
        assertTrue(isSorted);
    }
}
