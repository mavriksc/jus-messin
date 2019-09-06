package org.mavriksc.messin.hackerrank;

import java.util.Arrays;

class Prime{
    void  checkPrime(Integer... integers){
        StringBuilder sb = new StringBuilder();
        for (Integer i : integers) {
            if (isIt(i))
                sb.append(i).append(" ");
        }
        if(sb.length()>0)
            sb.deleteCharAt(sb.lastIndexOf(" "));
        System.out.println(sb.toString());
    }
    private boolean isIt(int n){
        if (n<=1)
            return false;
        else {
            for (int i = 2; i < n/2+1 ; i++) {
                if (n%i==0)
                    return false;
            }
        }
        return true;
    }
}
public class PrimeChecker {
    public static void main(String[] args) {
        Prime p = new Prime();
        p.checkPrime(1);
        p.checkPrime(1,2);
        p.checkPrime(1,2,3);
        p.checkPrime(1,2,3,4);
        p.checkPrime(1,2,3,4,5);
    }
}
