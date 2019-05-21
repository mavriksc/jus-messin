package org.mavriksc.messin;

public class NullCharArray {
    public static void main(String[] args){
        char[] bits = {'a','b','c','\0','\0','\0'};
        String s = String.valueOf(bits).trim();
        System.out.println(s);

    }
}
