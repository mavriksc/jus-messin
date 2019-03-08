package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OutputFormat {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        List<String> outs = new ArrayList<>();

        for(int i=0;i<3;i++)
        {
            String s1=sc.next();
            int x=sc.nextInt();

            outs.add( String.format("%-15s%03d",s1,x));
        }
        System.out.println("================================");
        outs.forEach(System.out::println);
        System.out.println("================================");

    }

}
