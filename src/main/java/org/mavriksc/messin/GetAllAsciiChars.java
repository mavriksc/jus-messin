package org.mavriksc.messin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetAllAsciiChars {
    public static void main(String[] args) {
        String  sourceText = "a whole bunch of characters and things like that";
        Map<Integer,Long> charCount = sourceText.chars().boxed().collect(Collectors.groupingBy(i->i,Collectors.counting()));
        charCount.forEach((k,v)->{
            char c = (char)k.intValue();
            System.out.println("the count for character `"+c+"` is:"+v);
        });


    }
}
