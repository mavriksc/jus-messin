package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableText {
    public static void main(String[] args) {
        Iterable<String> list
                = Arrays.asList("1 5 10", "1 6 20", "1 7 20", "1 8 11");
        Optional<String> answer = StreamSupport.stream(list.spliterator(),false)
                .max(Comparator.comparingInt(IterableText::getComTime));
        answer.ifPresent(System.out::println);

    }

    private static int getComTime(String line){
        String[] vals = line.split(" ");
        return  Integer.parseInt(vals[2]);
    }
}
