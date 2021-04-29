package org.mavriksc.messin.hackerrank;

import java.util.Scanner;

public class StaticInit {
    private static Scanner scanner = new Scanner(System.in);
    private static int B = getDimension();


    private static int H = getDimension();

    private static int getDimension() {
        return scanner.nextInt();
    }

    private static boolean flag = shouldPrint();

    private static boolean shouldPrint() {
        if (B <= 0 || H <= 0) {
            System.out.println("java.lang.Exception: Breadth and height must be positive");
            return false;
        }else return true;
    }


    public static void main(String[] args) {
        if (flag) {
            int area = B * H;
            System.out.print(area);
        }

    }//end of main

}//end of class


