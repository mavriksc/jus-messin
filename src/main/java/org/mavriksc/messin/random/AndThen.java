package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;

public class AndThen {
    public static void main(String[] args){
        List<String> hey = new ArrayList<>();
        hey.add("this");
        hey.add("that");
        hey.add("other thing");
        hey.forEach(s-> s+="!");

    }
}
