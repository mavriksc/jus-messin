package org.mavriksc.messin;

import java.util.Calendar;
import java.util.Date;

public class PrimeTiming {
    public static void main(String[] args){
        for (int i = 1; i < 750000; i++) {
            boolean ip = isPrime(i);
            Date now = Calendar.getInstance().getTime();
            System.out.println(now.getTime()%100_000+"\t"+i+"\t\t|\t\t"+ip);
        }
    }
    private static boolean isPrime(int n){
        if(n <= 1){
            return false;
        }

        if(n == 2) {
            return true;
        }

        if(n % 2 == 0){
            return false;
        }

        for(int i = 3; i <= Math.sqrt(n) + 1; i = i + 2){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }
}
