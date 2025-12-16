package org.mavriksc.messin.random;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FrogJump {
    public static final int TRIALS = 100000;
    private static int BANK = 10;

    public static void main(String[] args) {
        //System.out.println(IntStream.range(0, 100000).parallel().map(i -> getJumps()).average());
        Map<Integer,Long> countFqy = IntStream.range(0, TRIALS).parallel().mapToObj(i -> getJumps()).collect(Collectors.groupingBy(i->i,Collectors.counting()));
        final long[] total = { 0 };
        countFqy.forEach((k,v)->{
            total[0] += k*v;
            System.out.println(k+"\t"+v);
        });
        double avg = total[0]/(double)TRIALS;
        System.out.println(avg);
    }

    private static int getJumps() {
        int count = 0;
        int dist = 0;
        do {
            int jump = ThreadLocalRandom.current().nextInt((BANK - dist)) + 1;
            dist += jump;
            count++;
        } while (dist < BANK);
        return count;
    }
}
