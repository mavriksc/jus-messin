package org.mavriksc.messin.random;

import java.util.stream.IntStream;

public class WaysToSumNumber {
    static int countConsecutive(int num) {
        return (int) IntStream.range(1, (int) Math.sqrt(2 * num)).parallel().filter(i -> {
            float a = (float) ((1.0 * num - (i * (i + 1)) / 2) / (i + 1));
            return a - (int) a == 0.0;
        }).count();
    }

        public static void main (String[]args){
            int num = 15;
            System.out.println(countConsecutive(num));
        }
    }
