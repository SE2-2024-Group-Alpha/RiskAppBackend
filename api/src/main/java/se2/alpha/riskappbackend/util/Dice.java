package se2.alpha.riskappbackend.util;

import java.util.Random;

public class Dice {

        private static final int NUM_SIDES = 6;
        private Random random;

        public Dice() {
            random = new Random();
        }


        public int roll() {
            return random.nextInt(NUM_SIDES) + 1;
        }


        public int[] rollMultipleTimes(int numRolls) {
            int[] results = new int[numRolls];
            for (int i = 0; i < numRolls; i++) {
                results[i] = roll();
            }
            return results;
        }
    }


