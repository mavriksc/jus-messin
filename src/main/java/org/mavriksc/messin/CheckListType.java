package org.mavriksc.messin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckListType {

    public static void main(String[] args){
        List<Object> list = new ArrayList<Object>();
        list.add(Integer.valueOf(256));
        list.add(Float.valueOf(13.5f));
        list.add("this s right here");

        list.forEach(o-> System.out.println(o.toString()+"\t"+o.getClass()));
    }
}
