package org.mavriksc.messin.hackerrank;

public class BreakingRecords {

    static int[] breakingRecords(int[] scores) {
        int min = scores[0];
        int max = scores[0];
        int minCount = 0;
        int maxCount = 0;
        int val;
        for (int i = 1; i < scores.length; i++) {
            val = scores[i];
            if (val < min) {
                min = val;
                minCount++;
            } else if (val > max) {
                max = val;
                maxCount++;
            }
        }
        return new int[] { maxCount, minCount };
    }

}
