package org.mavriksc.messin;

import java.util.concurrent.ThreadLocalRandom;

public class RandSqrt {
    public static void main(String[] args){
        int N = 1000000;
        double norm= 0,sqrt=0,rand,thing,uniCirc=0;
        for (int i = 0;i<N;i++){
            rand = ThreadLocalRandom.current().nextDouble();
            norm+= rand;
            thing = rand + ThreadLocalRandom.current().nextDouble();
            uniCirc += thing>1?2-thing:thing;
            sqrt+= Math.sqrt(rand);
        }
        System.out.println("Avg rand 0<=x<1: "+(norm/N));
        System.out.println("Avg other teq 0<=x<1: "+(uniCirc/N));
        System.out.println("Avg sqrt(rand) 0<=x<1: "+(sqrt/N));
    }
}
