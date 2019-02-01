package org.mavriksc.messin.hackerrank;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class CountSort {



        // Complete the countSort function below.
        static void countSort(List<List<String>> arr) {
            StringBuilder sb = new StringBuilder();
            long start,end;
            start = new Date().getTime();
            IntStream.range(0,arr.size())
                    .mapToObj(i-> {if (i<arr.size()/2) {
                        arr.get(i).set(1,"-");
                    } return arr.get(i);
                    }).collect(Collectors.groupingBy(strings -> strings.get(0),
                    Collectors.mapping(i ->i.get(1), Collectors.toList())))
                    .entrySet().stream()
                    .sorted((kv1,kv2)->Integer.parseInt(kv1.getKey())-Integer.parseInt(kv2.getKey()))
                    .forEach(kv->kv.getValue().forEach(s -> sb.append(s).append(" ")));
            System.out.println(sb.toString());
            end = new Date().getTime();

            System.out.println("\n"+(end-start));
        }

        static void countSortONE(List<List<String>> arr) {
            StringBuilder sb = new StringBuilder();
            long start,end;
            start = new Date().getTime();
            arr.subList(0,arr.size()/2).parallelStream().forEach(l->l.set(1,"-"));

            int count = 0;
            arr.stream()
                    .collect(Collectors.groupingBy(strings -> strings.get(0),
                            Collectors.mapping(i ->i.get(1), Collectors.toList())))
                    .entrySet().stream()
                    .sorted((kv1,kv2)->Integer.parseInt(kv1.getKey())-Integer.parseInt(kv2.getKey()))
                    .forEach(kv->kv.getValue().forEach(s ->sb.append(s).append(" ")));


            System.out.println(sb.toString());
            end = new Date().getTime();

            System.out.println("\n"+(end-start));
        }

        public static void main(String[] args) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("D:/code/jus-messin/src/main/resources/input05.txt"));

            int n = Integer.parseInt(bufferedReader.readLine().trim());

            List<List<String>> arr = new ArrayList<>();

            IntStream.range(0, n).forEach(i -> {
                try {
                    arr.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                    .collect(toList())
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

           countSort(arr);
            //countSortONE(arr);

            bufferedReader.close();
        }

}
