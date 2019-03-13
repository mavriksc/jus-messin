package org.mavriksc.messin.hackerrank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
    public static void main(String[]args){
        Scanner s = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int count=0;
        while (s.hasNextLine()){
           String next = s.nextLine();
           if (next!=null)
               count++;
               sb.append(count).append(" ").append(next).append("\n");

        }

        System.out.println(sb.toString());
    }
}
