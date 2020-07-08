package org.mavriksc.messin;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Split {
    public static void main(String[] args) {
        String[] questionResponseCode = StringUtils.split("WI-FI--NOT_TESTED", "--");
        Arrays.asList(questionResponseCode).forEach(System.out::println);

    }
}
