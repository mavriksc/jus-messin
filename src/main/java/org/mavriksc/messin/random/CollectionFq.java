package org.mavriksc.messin.random;

import org.mavriksc.messin.objects.ThingObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionFq {

    public static void main(String[] args) {

        List<ThingObject> list = new ArrayList<>();
        list.add(new ThingObject("one"));
        list.add(new ThingObject("one"));
        list.add(new ThingObject("two"));
        list.add(new ThingObject("three"));
        list.add(new ThingObject("four"));
        Map<String, Long> asdf = list.stream()
                .collect(Collectors.groupingBy(ThingObject::getData, Collectors.counting()));
        asdf.forEach((k, v) -> System.out.println("key:" + k + " count:" + v));

     //   System.out.println("THINGSNIDNFOSDINFOSIDN".substring(0,1));

        List<Integer> things = Stream.of(1, 4, 6, 3, 7, 8, 20)
                .sorted(Comparator.comparingInt(Integer::intValue))
                .collect(Collectors.toList());

        for (Integer i:things) {
            System.out.println(i);
        }

    }


}
