package org.mavriksc.poc.app;

import org.mavriksc.poc.model.Deck;

import java.util.Date;

public class ShuffleOut {
    public static void main(String[] args){
        Deck d;
        Date oldStart = new Date();
        for (int i = 0; i < 1500; i++) {
            d = new Deck();
            d.shuffle();
        }
        Date oldEnd = new Date();

        Date newStart = new Date();
        for (int i = 0; i < 1500; i++) {
            d = new Deck();
            d.shuffleInPlace();
        }

        Date newEnd = new Date();

        long oldTime = oldEnd.getTime()-oldStart.getTime();
        long newTime = newEnd.getTime()-newStart.getTime();

        System.out.println(oldTime);
        System.out.println(newTime);


    }

}
