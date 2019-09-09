package org.mavriksc.messin.hackerrank;

import java.util.Calendar;
// https://www.hackerrank.com/challenges/day-of-the-programmer/problem
public class DayOfTheProgrammer {
    public static void main(String[] args) {

    }
    static String getDate(int year){
        int day=0;
        int month=0;
        if (year==1918){
            month = 10;
            day = 2;
        }else if (year<1918){

        }else {

        }
        return String.format("%02d.%02d.%d", day, month, year);
    }
}
