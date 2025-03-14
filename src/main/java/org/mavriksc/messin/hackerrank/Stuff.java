package org.mavriksc.messin.hackerrank;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Stuff {

    public static void main(String[] args) {


    }

    static List<Integer> compareTriplets(List<Integer> a, List<Integer> b) {
        int aScore = 0;
        int bScore = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).compareTo(b.get(i)) > 0)
                aScore++;
            else if (a.get(i).compareTo(b.get(i)) < 0)
                bScore++;
        }
        return Arrays.asList(aScore, bScore);

    }

    static long aVeryBigSum(long[] ar) {
        long sum = 0L;
        for (long l : ar) {
            sum += l;
        }
        return sum;


    }

    static int diagonalDifference(int[][] arr) {
        int answer = 0;
        for (int i = 0; i < arr.length; i++) {
            answer += arr[i][i] - arr[i][arr.length - 1 - i];
        }
        return Math.abs(answer);

    }

    static void plusMinus(int[] arr) {
        int neg = 0;
        int pos = 0;
        int zero = 0;
        for (int i : arr) {
            if (i < 0)
                neg++;
            else if (i > 0)
                pos++;
            else
                zero++;
        }
        System.out.println("" + (float) pos / arr.length);
        System.out.println("" + (float) neg / arr.length);
        System.out.println("" + (float) zero / arr.length);
    }

    static void staircase(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j < n - 1 - i)
                    System.out.print(" ");
                else
                    System.out.print("#");
            }
            System.out.println("");
        }
    }

    static void miniMaxSum(int[] arr) {
        Arrays.sort(arr);
        long high = (long) arr[4] + arr[3] + arr[2] + arr[1];
        long low = (long) arr[0] + arr[1] + arr[2] + arr[3];
        System.out.println("" + low + " " + high);

    }

    static int birthdayCakeCandles(int[] ar) {
        int max = Arrays.stream(ar).max().getAsInt();
        return (int) Arrays.stream(ar).filter(i -> i == max).count();

    }

    static String timeConversion(String s) {
        SimpleDateFormat hour12 = new SimpleDateFormat("hh:mm:ssa");
        SimpleDateFormat hour24 = new SimpleDateFormat("KK:mm:ss");
        Date convertMe = null;
        try {
            convertMe = hour12.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour24.format(convertMe);
    }

    static void countSort(List<List<String>> arr) {
        int len = Integer.parseInt(arr.get(0).get(0));
        assert len == arr.size();

    }

    static int simpleArraySum(int[] ar) {
        return Arrays.stream(ar).parallel().sum();

    }

    static String findDay(int month, int day, int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        System.out.println(c.getTime().toString());
        System.out.println(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase());
        return "";
    }

    static void currencyFormatter(double amount){

        String us = NumberFormat.getCurrencyInstance(Locale.US).format(amount);
        String india = NumberFormat.getCurrencyInstance(new Locale("en","IN")).format(amount);
        String china = NumberFormat.getCurrencyInstance(Locale.CHINA).format(amount);
        String france = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(amount);

        System.out.println("US: " + us);
        System.out.println("India: " + india);
        System.out.println("China: " + china);
        System.out.println("France: " + france);

    }


}

