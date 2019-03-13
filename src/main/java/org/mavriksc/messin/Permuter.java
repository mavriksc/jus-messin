package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Permuter {

    public static void main(String[] args) throws InterruptedException {
        List<String> source = asList("A", "B", "C", "D", "E");
        List<String> permutations = returnPermutations(source, 5, 0);
        System.out.println(permutations.size());
        Thread.sleep(2000);
        permutations.forEach(System.out::println);
    }

    private static List<String> returnPermutations(List<String> source, int choose, int len) {
        List<String> result = new ArrayList<>();

        if (len + 1 == choose) {
            return source;
        } else {
            for (String s : source) {
                List<String> tail = new ArrayList<>(source);
                tail.remove(s);
                result.addAll(returnPermutations(tail, choose, len+1).stream().map(ts -> s + ts)
                        .collect(Collectors.toList()));
            }
            return result;
        }
    }

}
