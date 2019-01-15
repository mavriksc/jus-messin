package org.mavriksc.messin;

public class TestStringTrunc {
    public static void main(String[] args) {
        PrivThing p = new PrivThing();
        p.info = "165132165103211             ";
        System.out.println(("---ORIGINAL---"));
        System.out.print(p.info);
        System.out.println(("|----end of string!"));

        System.out.println(("---PASS STRING---"));
        truncInfo(p.info);
        System.out.print(p.info);
        System.out.println(("|----end of string!"));
        System.out.println(("---PASS OBJECT---"));
        truncInfo(p);
        System.out.print(p.info);
        System.out.println(("|----end of string!"));

    }

    private static void truncInfo(PrivThing thing) {
        thing.info = thing.info.trim();
    }

    private static void truncInfo(String info) {
        info = info.trim();
    }
}

class PrivThing {
    String info;
}
