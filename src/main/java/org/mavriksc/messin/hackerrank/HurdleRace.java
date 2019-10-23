package org.mavriksc.messin.hackerrank;

import java.util.Arrays;

public class HurdleRace {
    static int hurdleRace(int k, int[] height) {

        return Math.max(Arrays.stream(height).max().getAsInt()-k,0);



    }
}
