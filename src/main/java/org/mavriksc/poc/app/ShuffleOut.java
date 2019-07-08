package org.mavriksc.poc.app;

import org.mavriksc.poc.model.Deck;

public class ShuffleOut {
    public static void main(String[] args){
        Deck d = new Deck();
        System.out.println(d.toString());
        d.shuffle();
        System.out.println(d.toString());

    }
}
