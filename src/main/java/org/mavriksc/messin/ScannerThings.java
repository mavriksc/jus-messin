package org.mavriksc.messin;

import java.util.Scanner;

public class ScannerThings {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = sc.next("\\S+\\s");
        System.out.println("  ---hello"+name);

    }
}
