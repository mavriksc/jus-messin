package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArraysAsList {


    public static void main (String... args){
        List<Long> l = Arrays.asList(100L, 500L);
        l.forEach(System.out::println);
        List<Long> in = l.stream().filter(lo-> 0<lo&& lo<10).collect(Collectors.toList());
        List<Long> out = new ArrayList<>(l);
        out.removeAll(in);


    }
}
