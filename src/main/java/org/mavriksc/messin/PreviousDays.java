package org.mavriksc.messin;

import java.time.LocalDate;
import java.time.ZoneId;

public class PreviousDays {
    public static final ZoneId zoneId = ZoneId.of("America/New_York");
    public static void main(String[] args){
        int noOfDays = 7;
        LocalDate previousDay = LocalDate.now(zoneId).minusDays(noOfDays);
        System.out.println(previousDay.atStartOfDay(zoneId));
    }
}
