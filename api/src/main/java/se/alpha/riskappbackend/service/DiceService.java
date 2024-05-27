package se.alpha.riskappbackend.service;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DiceService {

    private static final int NUM_SIDES = 6;
    private Random random = new Random();

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