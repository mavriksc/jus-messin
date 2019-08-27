package org.mavriksc.messin.hackerrank;

import java.util.Arrays;

public class CountApplesAndOranges {


    static void countApplesAndOranges(int leftEdge, int rightEdge, int aTree, int oTree, int[] apples, int[] oranges) {

        long appleHits = Arrays.stream(apples).filter(d -> d > 0)
                .filter(d -> aTree + d >= leftEdge && aTree + d <= rightEdge).count();
        long orangeHits = Arrays.stream(oranges).filter(d -> d < 0)
                .filter(d -> oTree + d > leftEdge && oTree + d < rightEdge).count();
        System.out.println(appleHits);
        System.out.println(orangeHits);
    }
}
