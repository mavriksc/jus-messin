package org.mavriksc.messin.hackerrank;

import java.util.*;
import java.io.*;

class Loopy{

    public static void main(String []argh){
        Scanner in = new Scanner(System.in);
        int t=in.nextInt();
        for(int i=0;i<t;i++){
            int a = in.nextInt();
            int b = in.nextInt();
            int n = in.nextInt();
            doThings(a,b,n);
        }
        in.close();

    }

    private static void doThings(int a, int b, int n) {
        StringBuilder sb = new StringBuilder();
        long[] answers = new long[n+1];
        answers[0]=a;
        for (int i = 1; i < n+1; i++) {
            answers[i]=answers[i-1]+(long)Math.pow(2,i-1)*b;
            sb.append(answers[i]).append(" ");
        }
        System.out.println(sb.toString().trim());


    }
}

