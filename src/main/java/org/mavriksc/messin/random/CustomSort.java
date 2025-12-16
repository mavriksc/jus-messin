package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomSort {

    public static void main(String[] args) {

        List<String> l1 = makeRandomList();
        System.out.println(l1.get(0));
        List<String> l2 = new ArrayList<>(l1);

        String order = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


        Comparator<String> comparatorNew = (s1, s2) -> {
            int len1 = s1.length();
            int len2 = s2.length();
            int lim = Math.min(len1, len2);
            int k = 0;
            while (k < lim) {
                char c1 = s1.charAt(k);
                char c2 = s2.charAt(k);
                if (c1 != c2) {
                    if ((c1 >= 'A' && c2 >= 'A') ^ (c1 <= '9' && c2 <= '9')) {
                        return c1 - c2;
                    } else {
                        return c2 - c1;
                    }
                }
                k++;
            }
            return len1 - len2;
        };
        Comparator<String> comparatorOld = (s1, s2) -> {
            int minLen = Math.min(s1.length(), s2.length());
            for (int i = 0; i < minLen; i++) {
                char s1Char = s1.charAt(i);
                char s2Char = s2.charAt(i);
                if (s1Char != s2Char) {
                    //if the characters are diff get precedence
                    return order.indexOf(s1Char) - order.indexOf(s2Char);
                }
            }
            // if all chars are the same the longer one is last
            return s1.length() - s2.length();
        };

        Date start;
        Date end;
        long t1;
        long t2;
        start = new Date();
        l1.sort(comparatorNew);
        end = new Date();
        t1 = end.getTime() - start.getTime();
        start = new Date();
        l2.sort(comparatorOld);
        end = new Date();
        t2 = end.getTime() - start.getTime();
        System.out.println("NEW METHOD:" + t1);
        System.out.println("OLD METHOD:" + t2);
        System.out.println(l1.get(0));
        long mismatches = IntStream.range(0,l1.size()).filter(i-> !l1.get(i).equals(l2.get(i))).count();
        if (mismatches>0)
            System.out.println("sort was not the same");
    }

    private static List<String> makeRandomList() {
        int min = 15;
        int max = 20;
        return IntStream.range(0, 1000000).mapToObj(i ->
                IntStream.range(0, ThreadLocalRandom.current().nextInt(min, max))
                        .map(x -> ThreadLocalRandom.current().nextInt(36))
                        .map(x -> x < 10 ? '0' + x : 'A' + x - 10)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString()
        ).collect(Collectors.toList());
    }


}
