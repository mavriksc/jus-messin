package main;

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
        Map<String,List<Item>> map = things.stream().collect(Collectors.groupingBy(Item::getD1,Collectors.mapping(i-> i, Collectors.toList())));
        System.out.println("done mapping");
    }
}

class Item {
    String d1;
    String d2;
    String d3;

    public Item(String d1, String d2, String d3) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    public String getD1() {
        return d1;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getD3() {
        return d3;
    }

    public void setD3(String d3) {
        this.d3 = d3;
    }

    @Override public String toString() {
        return "Item{" + "d1='" + d1 + '\'' + ", d2='" + d2 + '\'' + ", d3='" + d3 + '\'' + '}';
    }
}
