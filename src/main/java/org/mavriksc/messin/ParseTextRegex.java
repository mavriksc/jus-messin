package org.mavriksc.messin;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTextRegex {

    public static void main(String[] a) {
        Integer[] parsed = new Integer[7];
        String thing = "2   2   2       3   1   19";
        Pattern pattern = Pattern.compile("(\\d+| )(?:   )?");
        Matcher m = pattern.matcher(thing);
        int index = 0;
        while (m.find()) {
            if (!m.group(0).trim().isEmpty())
                parsed[index] = Integer.parseInt(m.group(0).trim());
            else
                parsed[index] = -1; //or what ever your missing data value should be.
            index++;
        }
        Arrays.asList(parsed).forEach(System.out::println);

    }

}
