package org.mavriksc.messin.hackerrank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GradingStudents {

    public static List<Integer> gradingStudents(List<Integer> grades) {
        return grades.stream().map(i -> {
            if (i < 38) {
                return i;
            } else if(i%5>2){
                return ((i/5)+1)*5;
            } else return i;
        }).collect(Collectors.toList());
    }

    public static void main(String[]a){
        gradingStudents(Arrays.asList(84,38,29)).forEach(System.out::println);
    }

}
