package org.mavriksc.messin;

import org.mavriksc.messin.objects.ThingObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    }
}
