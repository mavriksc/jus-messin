package org.mavriksc.messin.hackerrank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShortPalidrome {

    static int shortPalindrome(String s) {
        long count = 0;
        List<String> perms = new ArrayList<>();
        String ab = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < ab.length(); i++) {
            for (int j = 0; j < ab.length(); j++) {
                perms.add(ab.substring(i, i + 1) + ab.substring(j, j + 1) + ab.substring(j, j + 1) + ab
                        .substring(i, i + 1));
            }
        }
        for (String sub : perms) {
            count= (count+countMatches(s,sub)% 1000000007);

        }
        return (int) count% 1000000007;
    }

    static private int countMatches(String seq, String subseq) {
       int[][] tbl = new int[seq.length() + 1][subseq.length() + 1];

        for (int row = 0; row < tbl.length; row++)
            for (int col = 0; col < tbl[row].length; col++)
                tbl[row][col] = countMatchesFor(row, col,tbl,seq,subseq)% 1000000007;

        return tbl[seq.length()][subseq.length()];
    }

    static private int countMatchesFor(int seqDigitsLeft, int subseqDigitsLeft,int[][]tbl,String seq,String subseq) {
        if (subseqDigitsLeft == 0)
            return 1;

        if (seqDigitsLeft == 0)
            return 0;

        char currSeqDigit = seq.charAt(seq.length() - seqDigitsLeft);
        char currSubseqDigit = subseq.charAt(subseq.length() - subseqDigitsLeft);

        int result = 0;

        if (currSeqDigit == currSubseqDigit)
            result += tbl[seqDigitsLeft - 1][subseqDigitsLeft - 1];

        result += tbl[seqDigitsLeft - 1][subseqDigitsLeft];

        return result;
    }

}
