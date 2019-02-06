package org.mavriksc.messin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class LastYearWasSoRandom {

    public static void main(String[] args) {
        LocalDate then = LocalDate.now().minusYears(1);
        LocalDate now = LocalDate.now();
        long diff = now.toEpochDay() - then.toEpochDay();
//        for (int i = 0; i <50; i++) {
//            int rand = ThreadLocalRandom.current().nextInt((int) diff);
//            LocalDate randDate = then.plusDays(rand);
//            System.out.println(randDate);
//        }
        backInTime();
    }
    private static void backInTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-mm");
        Date nov5th1955 = null;
        try {
            nov5th1955 = sdf.parse("1955-05-11");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(nov5th1955.getTime());
        System.out.println(nov5th1955);

    }
}
