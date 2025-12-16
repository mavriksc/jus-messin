package org.mavriksc.messin.random;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

public class RandStrings {
    public static void main(String[] args){
        Integer[] nums = {1,2,5,6,9,5,5,4,8};
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomStringUtils.randomAlphabetic(8).replaceAll("I","i").replaceAll("l","L"));
        }
        System.out.println(countOccurencesofIntInarrayRec(nums,5));
    }

    private static int countOccurencesofIntInarrayRec(Integer[] nums, int target){
        if (nums == null || nums.length==0) return 0;
        int addNum = nums[0]==target?1:0;
        return  countOccurencesofIntInarrayRec(Arrays.copyOfRange(nums, 1, nums.length),target) + addNum;
    }
}
