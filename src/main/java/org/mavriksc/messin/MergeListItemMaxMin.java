package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MergeListItemMaxMin {

    public static void main(String[] args) {
        List<Integer> things = new ArrayList<>();
        things.add(3);
        things.add(2);
        things.add(8);
        things.add(2);
        things.add(9);
        System.out.println(merge(things, 2));
        things.clear();
        for (int i = 0; i < 10; i++) {
            things.add(ThreadLocalRandom.current().nextInt(20));
        }
        System.out.println(things);
        System.out.println(merge(things, 4));
    }

    private static List<Integer> merge(List<Integer> things, int merges) {
        List<Integer> result = new ArrayList<>(things);
        for (int i = 0; i < merges; i++) {
            int min = Integer.MAX_VALUE;
            List<Integer> positions = new ArrayList<>();
            for (int j = 0; j < result.size(); j++) {
                if (result.get(j) < min) {
                    positions.clear();
                    positions.add(j);
                    min = result.get(j);
                } else if (result.get(j) == min) {
                    positions.add(j);
                }
            }
            List<ValPosFrom> neighbors = new ArrayList<>();
            positions.forEach(p -> {
                if (p - 1 >= 0) {
                    neighbors.add(new ValPosFrom(result.get(p - 1), p - 1, p));
                }
                if (p + 1 < result.size()) {
                    neighbors.add(new ValPosFrom(result.get(p + 1), p + 1, p));
                }
            });
            ValPosFrom vpf = Collections.min(neighbors, Comparator.comparingInt(v -> v.val));
            result.set(vpf.pos, result.get(vpf.pos) + result.get(vpf.from));
            result.remove(vpf.from);
        }
        return result;
    }
}

class ValPosFrom {
    int val;
    int pos;
    int from;

    ValPosFrom(int val, int pos, int from) {
        this.val = val;
        this.pos = pos;
        this.from = from;
    }
}