package org.mavriksc.messin.random;

import java.util.Arrays;

public class SumConsecutivePairs {
    public static void main(String[] args) {
        int[] array = { 11, 6, 87, 32, 15, 5, 9, 21 };
        int sum = doTheThing(array);
        System.out.println(sum);

    }


    static private int doTheThing(int[] list){
        if (list.length==2)
            return list[0]+list[1];
        return list[0]+list[1]+doTheThing(Arrays.copyOfRange(list,1,list.length));
    }

}
