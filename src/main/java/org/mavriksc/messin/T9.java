package org.mavriksc.messin;

import java.util.*;

public class T9 {

    private static Map<String, List<String>> t9;

    public static void main(String[] args) {
        String translate = "test test test";
        String complete = translate(translate);
        System.out.println(complete);
        System.out.println(complete.length());

    }

    private static String translate(String input) {
        List<String> chars = Arrays.asList(input.toLowerCase().split(""));
        StringBuilder sb = new StringBuilder();
        for (String c : chars) {
            String k = "";
            int presses = 0;
            for (Map.Entry e : getT9().entrySet()) {
                if (((List<String>) e.getValue()).contains(c)) {
                    k = (String) e.getKey();
                    presses = ((List<String>) e.getValue()).indexOf(c) + 1;
                }
            }
            if ("".equals(k) || presses == 0) {
                throw new RuntimeException("oops");
            }
            for (int i = 0; i < presses; i++) {
                sb.append(k);
            }
        }
        return sb.toString();
    }

    private static Map<String, List<String>> getT9() {
        if (t9 == null) {
            t9 = new HashMap<>();
            t9.put("2", Arrays.asList("a", "b", "c"));
            t9.put("3", Arrays.asList("d", "e", "f"));
            t9.put("4", Arrays.asList("g", "h", "i"));
            t9.put("5", Arrays.asList("j", "k", "l"));
            t9.put("6", Arrays.asList("m", "n", "o"));
            t9.put("7", Arrays.asList("p", "q", "r", "s"));
            t9.put("8", Arrays.asList("t", "u", "v"));
            t9.put("9", Arrays.asList("w", "x", "y", "z"));
            t9.put("#", Collections.singletonList(" "));
        }
        return t9;
    }
}
