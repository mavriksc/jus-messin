package org.mavriksc.messin;

import java.util.Arrays;
import java.util.List;

public class ArraysAsList {


    public static void main (String... args){
        List<Long> l = Arrays.asList(new Long(100),new Long(500));
        l.forEach(lon->System.out.println(lon));
    }
}
