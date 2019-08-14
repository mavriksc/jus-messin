package org.mavriksc.messin;

public class NullBoolean {
    public static void main(String[] a) {
        Boolean b = null;
        String s = b != null && b ? "true" : "false";
        System.out.println(s);
    }
}
