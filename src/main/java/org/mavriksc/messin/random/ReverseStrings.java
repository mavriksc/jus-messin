package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.List;

public class ReverseStrings {


    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("Wrong phone model - you entered Samsung SM-G900A Galaxy S5 16GB - ATT ( Samsung-SM-G900A, SM-G900A, SMG900A )  but phone was actually Samsung SM-G870A Galaxy S5 Active ( Samsung-SM-G870A, SM-G870A, SMG870A ), Phone is broken");
        list.add("Wrong phone model - you entered Samsung SM-G900A Galaxy S5 16GB - ATT ( Samsung-SM-G900A, SM-G900A, SMG900A )  but phone was actually Samsung SM-G870A Galaxy S5 Active ( Samsung-SM-G870A, SM-G870A, SMG870A )");
        list.add("Wrong phone model - you entered Apple A1633 iPhone 6s 128GB - AT&T but phone was actually Apple A1453 iPhone 5S 16GB - Other");
        list.add("Wrong phone model - you entered Apple A1633 iPhone 6s 128GB - AT&T but phone was actually Apple A1453 iPhone 5S 16GB - Other, Phone is broken");
        list.add("Wrong phone model - you entered Samsung SM-G870A Galaxy S5 Active 16GB - AT&T but phone was actually Samsung SCH-R530 Galaxy S III 32GB - Other, Phone does not power on");
        list.forEach(s-> System.out.println(new StringBuilder(s).reverse().toString()));

    }
}
