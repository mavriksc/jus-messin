package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;

public class FilterUpdate {
    public static void main(String[] args) {
        List<Customer> things = new ArrayList<>();
        things.add(new Customer("thing", "TX", 5));
        things.add(new Customer("thing", "TX", 2));
        things.add(new Customer("asdf", "MN", 5));
        things.add(new Customer("dfghfgf", "MN", 2));
        things.add(new Customer("nm,nm,nm", "HI", 5));
        things.add(new Customer("thxcvxcvxcing", "TX", 5));
        editNote(things,new Customer("thxcvxcvxcing", "XX", 5));
        things.forEach(c-> System.out.println(c.getState()));

    }

    public static void editNote(List<Customer> item, Customer c) {
        item.stream()
                .filter( n-> n.getName().equals(c.getName()))
                .findAny()
                .ifPresent(n -> {
                    n.setState(c.getState());
                });

    }
}
