package org.mavriksc.messin.hrcrap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


class Result {

    /*
     * Complete the 'fizzBuzz' function below.
     *
     * The function accepts INTEGER n as parameter.
     */

    public static void fizzBuzz(int n) {
        // Write your code here
        Map<Integer, String> rules = new HashMap<Integer, String>() {{
            put(15, "FizzBuzz");
            put(5, "Buzz");
            put(3, "Fizz");
        }};
        for (int i = 1; i <= n; i++) {
            int q = i;
            Optional<Integer> rule = rules.keySet().stream().sorted().filter(it -> q % it == 0).max(Integer::compareTo);
            if (!rule.isPresent()) {
                System.out.println(q);
            } else {
                System.out.println(rules.get(rule.get()));
            }

        }

    }

}

public class Solution {
    public static void main(String[] args) throws IOException {


        Result.fizzBuzz(65);

    }
}