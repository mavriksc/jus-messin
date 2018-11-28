package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListFilter {
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("hi");
        list.add("what");
        list.add("who");
        list.add("ok");

        List<String> noTwos = list.stream().filter(s-> s.length() != 2).collect(Collectors.toList());

        list.iterator().toString();
        System.out.println(noTwos);
    }
}
