package org.mavriksc.messin;

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

        Comparator<String> c = (s1, s2) -> {
            int len1 = s1.length();
            int len2 = s2.length();
            int lim = Math.min(len1, len2);
            char[] v1 = s1.toCharArray();
            char[] v2 = s2.toCharArray();

            int k = 0;
            while (k < lim) {
                char c1 = v1[k];
                char c2 = v2[k];
                if (c1 != c2) {
                    if ((c1 >= 'A' && c2 >= 'A') ^ (c1 <= '9' && c2 <= '9')) {
                        return c1 - c2;
                    } else {
                        System.out.println(c2 - c1);
                        return c2 - c1;
                   
                    }
                }
                k++;
            }
            return len1 - len2;
        };
        Arrays.sort(arr, c);
        for (
                String s : arr) {
            System.out.println(s);
        }
    }
}
