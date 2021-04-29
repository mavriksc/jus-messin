package org.mavriksc.messin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirstOfMonthList {
    public static void main(String[] args){
        List<Date> firstsOfMonths = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        cal.set(2019,Calendar.JANUARY,1);
        firstsOfMonths.add(cal.getTime());
        cal.set(2019,Calendar.FEBRUARY,1);
        firstsOfMonths.add(cal.getTime());
        firstsOfMonths.forEach(System.out::println);

    }
}
