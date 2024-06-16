package se.alpha.riskappbackend.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import se.alpha.riskappbackend.service.DiceService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiceServiceTest {

    @InjectMocks
    private DiceService diceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRoll() {
        int result = diceService.roll();
        assertTrue(result >= 1 && result <= 6, "Result should be between 1 and 6");
    }

    @Test
    public void testRollMultipleTimes() {
        int numRolls = 10;
        int[] results = diceService.rollMultipleTimes(numRolls);
        assertTrue(results.length == numRolls, "The results array should have the same length as numRolls");

        for (int result : results) {
            assertTrue(result >= 1 && result <= 6, "Each result should be between 1 and 6");
        }
    }

    @Test
    public void testRollMultipleTimesWithDifferentSizes() {
        int[] sizes = {1, 5, 10, 100};
        for (int size : sizes) {
            int[] results = diceService.rollMultipleTimes(size);
            assertTrue(results.length == size, "The results array should have the same length as the number of rolls");

            for (int result : results) {
                assertTrue(result >= 1 && result <= 6, "Each result should be between 1 and 6");
            }
        }
    }
}