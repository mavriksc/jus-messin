package org.mavriksc.messin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SumKeyBasedOnValue {

    public static void main(String[] args){
        Map<Integer,Integer> inventory = new HashMap<>();
        Map<Integer,Integer> consolidate = consolidateInventory(inventory);
    }

    private static Map<Integer, Integer> consolidateInventory(Map<Integer, Integer> inventory) {
        return inventory.entrySet().stream().collect(Collectors.groupingBy(Entry::getValue,Collectors.summingInt(Entry::getValue)));
    }

}
