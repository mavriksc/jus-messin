package org.mavriksc.messin;

public class Pathing {

    public static void main(String[] args) {

        System.out.println("Turns to mine 900:\t" + turnsToMine(900));
        System.out.println("Turns to mine 600:\t" + turnsToMine(600));
        System.out.println("Turns to mine 300:\t" + turnsToMine(300));
        System.out.println("Turns to mine 100:\t" + turnsToMine(100));
        System.out.println("Turns to mine 60:\t" + turnsToMine(60));
        System.out.println("Turns to mine 30:\t" + turnsToMine(30));
        System.out.println("Turns to mine 10:\t" + turnsToMine(10));
        System.out.println("Turns to mine 6:\t" + turnsToMine(6));
        System.out.println("Turns to mine 3:\t" + turnsToMine(3));

    }

    private static int turnsToMine(int halite) {
        if (halite <= 0) {
            return 0;
        } else {
            int getsMined = (int) Math.ceil((float) halite / 4);
            System.out.println("Gets mined: " + getsMined);
            return 1 + turnsToMine(halite - getsMined);
        }
    }
}
