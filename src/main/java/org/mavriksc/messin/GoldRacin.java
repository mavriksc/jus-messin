package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GoldRacin {
    private static final int N = 6;
    private static final int MAX_RAND = 10;
    private static int recurseCount = 0;
    private static int dynamicCount = 0;

    public static void main(String[] args) {
        List<List<Integer>> pyramid = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            List<Integer> level = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                level.add(ThreadLocalRandom.current().nextInt(MAX_RAND));
            }
            pyramid.add(level);
        }
        int slrSolution = sumLeftRightAndSelf(pyramid, 0, 0);
        System.out.println("Recursive solution: " + slrSolution + " and the steps:" + recurseCount);
        int dynamicSolution = dynamicSolution(pyramid);
        System.out.println("Dynamic solution: " + dynamicSolution + " and the steps:" + dynamicCount);
    }

    private static int sumLeftRightAndSelf(List<List<Integer>> pyramid, int level, int position) {
        recurseCount++;
        int currPos = pyramid.get(level).get(position);
        if (level + 1 < pyramid.size()) {
            int leftSum = sumLeftRightAndSelf(pyramid, level + 1, position);
            int rightSum = sumLeftRightAndSelf(pyramid, level + 1, position + 1);
            return leftSum > rightSum ? currPos+leftSum : currPos+rightSum;
        }
        return currPos;
    }

    private static int dynamicSolution(List<List<Integer>> pyramid) {
        List<List<Integer>> table = new ArrayList<>();

        for (int i = pyramid.size() - 2; i >= 0; i--) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < pyramid.get(i).size(); j++) {
                dynamicCount++;
                if (i == pyramid.size() - 2) {
                    row.add(pyramid.get(i).get(j) + Math.max(pyramid.get(i + 1).get(j), pyramid.get(i + 1).get(j + 1)));
                } else {
                    row.add(pyramid.get(i).get(j) + Math.max(table.get(table.size()-1).get(j), table.get(table.size()-1).get(j+1)));
                }

            }
            table.add(row);
        }
        Collections.reverse(table);
        return table.get(0).get(0);
    }
}
