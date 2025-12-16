package org.mavriksc.messin.random;

import java.util.concurrent.TimeUnit;

public class MyTimeUnit {
    public static void main(String[] args){
        System.out.println(TimeUnit.DAYS.toMillis(30));
        System.out.println(TimeUnit.DAYS.toMillis(1)*30);
    }
}
