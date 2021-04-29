package org.mavriksc.messin.hackerrank;

import java.util.Arrays;
import java.util.List;

public class MigratoryBirds {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,4,3,2,1,3,4);
        System.out.println(migratoryBirds(list));
    }

    static int migratoryBirds(List<Integer> arr) {
        int[] counts = new int[6];

        for (Integer integer : arr) {
            counts[integer]++;
        }
        int maxVal=-99;
        int maxPos = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i]>maxVal) {
                maxPos = i;
                maxVal= counts[i];
            }

        }
        return maxPos;
    }
}
