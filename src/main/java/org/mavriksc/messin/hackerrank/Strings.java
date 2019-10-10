package org.mavriksc.messin.hackerrank;

public class Strings {
    public static void main(String[] args) {
        String A="asdf";
        String B="ab";
        System.out.println(A.length()+B.length());
        System.out.println(A.compareTo(B)<=0?"NO":"YES");
        String out=A.substring(0,1).toUpperCase()+A.substring(1,A.length())+" "+B.substring(0,1).toUpperCase()+B.substring(1,B.length());
        System.out.println(out);
    }
}
