package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;

public class SafeWrap {
    public static void main(String[] args){
        descriptionFormatter("hello world apple orange grapes juice spaghetti sauce milk",34).forEach(System.out::println);
    }

    private static List<String> descriptionFormatter(String string, int amt){
        String[] split = string.split(" ");
        List<String> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (sb.length() + s.length() > amt) {
                ans.add(sb.toString());
                sb.setLength(0);
            } else {
                if (sb.length()!=0)
                    sb.append(" ");
                sb.append(s);
            }
        }
        ans.add(sb.toString());
        return ans;
    }
}
