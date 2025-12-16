package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoopTest {
    private static List<Integer> list;
    private static final int size=1000;

    public static void main(String[] args) {

    }
    private static void genList(){
        list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            list.set(i, ThreadLocalRandom.current().nextInt(0,200));
        }
    }
}
