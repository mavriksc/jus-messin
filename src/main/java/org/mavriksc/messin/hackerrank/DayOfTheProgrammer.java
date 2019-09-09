package org.mavriksc.messin.hackerrank;

import java.util.Calendar;
// https://www.hackerrank.com/challenges/day-of-the-programmer/problem
public class DayOfTheProgrammer {
    public static void main(String[] args) {
        System.out.println(getDate(1984));

    }
    static String getDate(int year){
        int day=0;
        int month=0;
        if (year==1918){
            month = 10;
            day = 2;
        }else if (year<1918){
            month = 9;
            day = year%4==0?12:11;

        }else {
            month = 9;
            day = year%400==0||(year%4==0&&year%100!=0)?12:11;
        }
        return String.format("%02d.%02d.%d", day, month, year);
    }
}
