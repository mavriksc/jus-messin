package org.mavriksc.messin.random;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ThreadRace {

    static Map<Integer, Date> results = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        IntStream.range(0,100).parallel().forEach(ThreadRace::doStuff);
        Thread.sleep(300);
        results.forEach((key, value) -> System.out.println(key + " " + value.getTime()));
    }

    private static void doStuff(int i) {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        results.put(i,new Date());
    }
}
