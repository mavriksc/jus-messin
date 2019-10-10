package org.mavriksc.messin;

public class ClazzString {
    public static void main(String[] args) {
        System.out.println(ClazzString.class.toString().replaceAll(".*\\.",""));
    }
}
