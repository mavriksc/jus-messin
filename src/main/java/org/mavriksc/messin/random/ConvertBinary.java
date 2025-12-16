package org.mavriksc.messin.random;

import java.util.Scanner;

public class ConvertBinary {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("What is the number in the decimal system that you want to convert to binary?");
        int dec = input.nextInt();
        int sqr = 1024;
        int rem;
        int bits = (int) Math.floor(Math.log((double) dec) / Math.log((double) 2));
        System.out.println("BITS:" + bits);
        while (bits > 0) {
            int twoPow = (int) Math.pow((double) 2, (double) bits);
            rem = dec / twoPow;
            dec = dec - rem * twoPow;
            bits--;
            System.out.print(rem);
        }
    }

}
