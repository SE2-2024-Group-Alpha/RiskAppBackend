package se.alpha.riskappbackend.model.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Dice {

    private static final int NUM_SIDES = 6;
    private static Random random = new Random();

    public Dice() {
        random = new Random();
    }

    // In your Dice class
    public int getNumSides() {
        return NUM_SIDES;
    }


    public static Integer roll() {
        return random.nextInt(NUM_SIDES) + 1;
    }


    public static Integer[] rollMultipleTimes(Integer numRolls) {
        Integer[] results = new Integer[numRolls];
        for (int i = 0; i < numRolls; i++) {
            results[i] = roll();
        }
        Arrays.sort(results, Collections.reverseOrder());
        return results;
    }

    public static int[] rollMultipleTimes(int numRolls) {
        int[] results = new int[numRolls];
        for (int i = 0; i < numRolls; i++) {
            results[i] = roll();
        }
        return results;
    }

    public int[] rollMultiple(int numRolls) {
        int[] results = new int[numRolls];
        for (int i = 0; i < numRolls; i++) {
            results[i] = roll();
        }
        return results;
    }
}


