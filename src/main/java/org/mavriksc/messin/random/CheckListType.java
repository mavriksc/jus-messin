package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;

public class CheckListType {

    public static void main(String[] args){
        List<Object> list = new ArrayList<>();
        list.add(256);
        list.add(13.5);
        list.add("this s right here");

        list.forEach(o-> System.out.println(o.toString()+"\t"+o.getClass()));
    }
}
