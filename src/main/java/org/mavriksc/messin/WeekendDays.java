package org.mavriksc.messin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class WeekendDays {
    public static void main(String[] args) {
        LocalDate start = LocalDate.parse("2019-02-28");
        LocalDate end = LocalDate.parse("2019-03-28");

        Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .filter(day -> day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY))
                .forEach(System.out::println);
    }

}
