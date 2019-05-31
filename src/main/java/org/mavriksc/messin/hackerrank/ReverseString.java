package org.mavriksc.messin.hackerrank;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class ReverseString {
    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        String A=sc.next();
        /* Enter your code here. Print output to STDOUT. */
        if (palindrome(A)){
            System.out.println("Yes");
        }else System.out.println("No");



    }
    private static boolean palindrome(String s){
        for (int i = 0; i < s.length()/2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - i)) {
            return false;
            }
        }
        return true;
    }
}
