package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterMapCount {

    public static void main(String[] args) {
        List<Customer> things = new ArrayList<>();
        things.add(new Customer("thing", "TX", 5));
        things.add(new Customer("thing", "TX", 2));
        things.add(new Customer("asdf", "MN", 5));
        things.add(new Customer("dfghfgf", "MN", 2));
        things.add(new Customer("nm,nm,nm", "HI", 5));
        things.add(new Customer("thxcvxcvxcing", "TX", 5));
        System.out.println(mapStuff(things, 5));
    }

    private static Map<String, Long> mapStuff(List<Customer> customers, Integer month) {
        return customers.stream()
                .filter(customer -> customer.getBDayMonth() == month)
                .collect(Collectors.groupingBy(Customer::getState, Collectors.counting()));
    }

}
