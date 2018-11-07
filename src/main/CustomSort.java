package main;

import java.util.Arrays;
import java.util.Comparator;

public class CustomSort {

    public static void main(String[] args) {
        String[] arr = {"2T1BURHE1JCO24154C",
                "2TABURHE1JC024154C",
                "JTDKARFP5H3055472C",
                "2T2BURHE1JCO24154C",
                "JTDKARFP1H3056246C",
                "JTDKARFP1H3056246"};
        String order = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Comparator<String> c = (s1, s2) -> {
            int minLen = Math.min(s1.length(), s2.length());
            for (int i = 0; i < minLen; i++) {
                char s1Char = s1.charAt(i);
                char s2Char = s2.charAt(i);
                if (s1Char == s2Char) {
                    //if characters are the same skip
                    continue;
                } else {
                    //if the characters are diff get precedence
                    return order.indexOf(s1Char) - order.indexOf(s2Char);
                }
            }
            // if all chars are the same the longer one is last
            return s1.length() - s2.length();
        };
        Arrays.sort(arr,c);
        for (String s : arr) {
            System.out.println(s);
        }
    }
}