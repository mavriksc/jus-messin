package org.mavriksc.messin.hackerrank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CountSort {


    // Complete the countSort function below.
    static void countSort(List<List<String>> arr) {
        final String hy = "-";
        StringBuilder sb = new StringBuilder();

        IntStream.range(0, arr.size())
                .mapToObj(i -> {
                    if (i < arr.size() / 2) {
                        arr.get(i).set(1, hy);
                    }
                    return arr.get(i);
                }).collect(Collectors.groupingBy(strings -> strings.get(0),
                Collectors.mapping(i -> i.get(1), Collectors.toList())))
                .entrySet().stream()
                .sorted(Comparator.comparingInt(kv -> Integer.parseInt(kv.getKey())))
                .forEach(kv -> kv.getValue().forEach(s -> sb.append(s).append(" ")));
        System.out.println(sb);

    }

    static void countSortONE(List<List<String>> arr) {
        final String hy = "-";
        StringBuilder sb = new StringBuilder();
        arr.subList(0, arr.size() / 2).parallelStream().forEach(l -> l.set(1, hy));

        arr.stream()
                .collect(Collectors.groupingBy(strings -> strings.get(0),
                        Collectors.mapping(i -> i.get(1), Collectors.toList())))
                .entrySet().stream()
                .sorted(Comparator.comparingInt(kv -> Integer.parseInt(kv.getKey())))
                .forEach(kv -> kv.getValue().forEach(s -> sb.append(s).append(" ")));


        System.out.println(sb);
    }

    public static void main(String[] args) throws IOException {
        long end, startAlg;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ClassLoader.getSystemResource("input05.txt").getFile()))) {

            int n = Integer.parseInt(bufferedReader.readLine().trim());

            List<List<String>> arr = new ArrayList<>();

            IntStream.range(0, n).forEach(i -> {
                try {
                    arr.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                    .collect(toList())
                    );
                } catch (IOException ex) {
                    throw new IllegalArgumentException(ex);
                }
            });
            startAlg = new Date().getTime();
            countSort(arr);
            end = new Date().getTime();

            System.out.println("ALG:" + (end - startAlg));
        }
    }

}
