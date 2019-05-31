package org.mavriksc.messin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

public class FrenchTime {
    public static void main(String[] args) throws ParseException {
        String date = "dimanche, 24. mars 2019";
        String year = date.split(" ")[date.split(" ").length-1];
        SimpleDateFormat df = new SimpleDateFormat( "EEEE, dd. MMMM yyyy", Locale.FRANCE);
        LocalDate d = df.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        System.out.println(d.getYear());
    }
}
