package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiMapCollector {

    public static void main(String[] args) {
        List<Item> things = new ArrayList<>();
        things.add(new Item("1234","stats","more stuff"));
        things.add(new Item("1234","stats2","more stuff"));
        things.add(new Item("4321","stats","more stuff"));
        things.add(new Item("4321","stats2","more stuff"));
        things.add(new Item("5432","stats","more stuff"));
        Map<String,List<Item>> map = things.stream().collect(Collectors.groupingBy(
                Item::getD1,Collectors.mapping(i-> i, Collectors.toList())));
        System.out.println("done mapping:" + map);
    }
}

class Item {
    private String d1;
    private String d2;
    private String d3;

    Item(String d1, String d2, String d3) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    String getD1() {
        return d1;
    }

    @Override public String toString() {
        return "Item{" + "d1='" + d1 + '\'' + ", d2='" + d2 + '\'' + ", d3='" + d3 + '\'' + '}';
    }
}
