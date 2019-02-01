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
            final String hy ="-";
            StringBuilder sb = new StringBuilder();

            IntStream.range(0,arr.size())
                    .mapToObj(i-> {if (i<arr.size()/2) {
                        arr.get(i).set(1,hy);
                    } return arr.get(i);
                    }).collect(Collectors.groupingBy(strings -> strings.get(0),
                    Collectors.mapping(i ->i.get(1), Collectors.toList())))
                    .entrySet().stream()
                    .sorted((kv1,kv2)->Integer.parseInt(kv1.getKey())-Integer.parseInt(kv2.getKey()))
                    .forEach(kv->kv.getValue().forEach(s -> sb.append(s).append(" ")));
            System.out.println(sb.toString());

        }

        static void countSortONE(List<List<String>> arr) {
            final String hy ="-";
            StringBuilder sb = new StringBuilder();
            arr.subList(0,arr.size()/2).parallelStream().forEach(l->l.set(1,hy));

            int count = 0;
            arr.stream()
                    .collect(Collectors.groupingBy(strings -> strings.get(0),
                            Collectors.mapping(i ->i.get(1), Collectors.toList())))
                    .entrySet().stream()
                    .sorted((kv1,kv2)->Integer.parseInt(kv1.getKey())-Integer.parseInt(kv2.getKey()))
                    .forEach(kv->kv.getValue().forEach(s ->sb.append(s).append(" ")));


            System.out.println(sb.toString());
        }

        public static void main(String[] args) throws IOException {
            long startread,end,startAlg;
            //startread = new Date().getTime();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ClassLoader.getSystemResource("input05.txt").getFile()));

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
            startAlg = new Date().getTime();
            countSort(arr);
//            countSortONE(arr);
            end = new Date().getTime();

            // System.out.println("READ:"+(startAlg-startread));
            System.out.println("ALG:"+(end-startAlg));
            // System.out.println("TOT:"+(end-startread));
            bufferedReader.close();
        }

}
