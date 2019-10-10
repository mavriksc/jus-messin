package org.mavriksc.messin.hackerrank;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SockMerchant {
    public static void main(String[] args) {
        System.out.println(sockMerchant(5, new int[] {10,10,10,50,20,99,50,98,20}));
        System.out.println(sockMerchant(5, new int[] {10,10,10,50,20,99,50,98}));
        System.out.println(sockMerchant(5, new int[] {10,10,10}));


    }

    static int sockMerchant(int n, int[] ar) {
        return IntStream.of(ar)
                .boxed()
                .collect(Collectors.groupingBy(i->i,Collectors.counting()))
                .values().stream()
                .map(tot->tot/2)
                .reduce(0L, Long::sum)
                .intValue();

    }
}
