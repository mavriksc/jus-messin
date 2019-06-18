package org.mavriksc.messin;

public class SplitNotFound {
    public static void main(String[] s) {
        String thing = "asdfasdfasdfasdfasdfasdfsheiueenjnfjfiuhfifn";
        String[] thing2 = thing.split("\n");
        for (String s1 : thing2) {
            System.out.println(s1);
        }
    }
}
