package org.mavriksc.messin;

public class DromeCheck {

    public static void main(String[] args){
        String s = "123412314231243121342132413214321";
        StringBuilder s2 = new StringBuilder(s);
        System.out.println(s.equals(s2.reverse().toString()));
    }
}
