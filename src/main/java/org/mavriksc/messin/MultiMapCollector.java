package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiMapCollector {

    public static void main(String[] args) {
        List<Item> things = new ArrayList<>();
        things.add(new Item("1234", "stats", "more stuff", 100));
        things.add(new Item("1234", "stats2", "more stuff", 50));
        things.add(new Item("4321", "stats", "more stuff", 80));
        things.add(new Item("4321", "stats2", "more stuff", 40));
        things.add(new Item("5432", "stats", "more stuff", 88));
        //Making a map from List of Items where Key is the item id and the value is  a list of items with that id.
        Map<String, List<Item>> map = things.stream().collect(Collectors.groupingBy(
                Item::getD1, Collectors.mapping(i -> i, Collectors.toList())));
        System.out.println("done mapping:" + map);
        // mapping items by id to their average score.
        Map<String, Double> map2 = things.stream().collect(Collectors.groupingBy(Item::getD1, Collectors.averagingDouble(Item::getScore)));
        map2.forEach((s, d) -> System.out.println(s + "'s average=" + d));

    }
}

class Item {
    private String d1;
    private String d2;
    private String d3;
    private double score;

    Item(String d1, String d2, String d3, double score) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    String getD1() {
        return d1;
    }


    @Override
    public String toString() {
        return "Item{" + "d1='" + d1 + '\'' + ", d2='" + d2 + '\'' + ", d3='" + d3 + '\'' + '}';
    }
}
