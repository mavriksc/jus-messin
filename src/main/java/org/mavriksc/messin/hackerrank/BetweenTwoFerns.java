package org.mavriksc.messin.hackerrank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BetweenTwoFerns {

    static int getTotalX(List<Integer> a, List<Integer> b) {
        b.sort(Comparator.naturalOrder());
        return (int) IntStream.range(1,b.get(0)+1).parallel()
                .filter(i->b.parallelStream().allMatch(n->n%i==0))
                .filter(i->a.parallelStream().allMatch(n->i%n==0))
                .count();

    }

    public static void main(String[] a){
        System.out.println(getTotalX(Arrays.asList(2,4),Arrays.asList(16,32,96)));
    }
}