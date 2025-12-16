package org.mavriksc.messin.random;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeThings {
    public static void main(String[] args) {
        Date day = DatatypeConverter.parseDateTime("2016-11-17").getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        System.out.println(cal.get(Calendar.HOUR));
        System.out.println(cal.get(Calendar.SECOND));
        System.out.println(cal.get(Calendar.MINUTE));
        cal.add(12,Calendar.HOUR);
        System.out.println(day);
    }

}
