package org.mavriksc.messin.random;

import java.util.HashMap;
import java.util.Map;

public class PairLetters {
    public static void main(String[] args) {
        String input = "cad bed abed dada";
        Map<String, Integer> table = new HashMap<>();
        String last = "";
        for (char c : input.toCharArray()) {
            if (last.isEmpty()) {
                last = String.format("%c", c);
                continue;
            }
            String thing = last + c;
            Integer count = table.getOrDefault(thing, 0);
            table.put(thing, count + 1);
            last = String.format("%c", c);
        }
    }
}
