package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Permuter {
    private static List<String> source = asList("A", "B", "C", "D", "E","F");
    private static int N = source.size();

    public static void main(String[] args) throws InterruptedException {



        //List<String> permutations = returnPermutations(source, N, 0);
        String[][] permutations = permutationsViaRotation(source);
        List<String> permArray = convertToArray(permutations);
       // String guess = "ABCDEABCDAEBCDABECDABCEDABCADEBCADBECADBCEADBCAEDBCABDECABDCEABDCAEBDCABEDCABACDEBACDBEACDBAECDBACEDBACBDEACBDAECBDACEBDACBEDACBADECBADCEBADCBEADCBAEDCBA";
//        StringBuilder sb = new StringBuilder(guess);
//        String reverse = sb.reverse().toString();
        //System.out.println(reverse);
        //System.out.println(isSuperPermutation(permArray, guess));
        //System.out.println(isSuperPermutation(permArray,reverse));
        outputPerm2DArray(permutations);
    }

    private static void outputPerm2DArray(String[][] permutations) {
        StringBuilder sb = new StringBuilder();
        for (String[] permutation : permutations) {
            for (String s : permutation) {
                sb.append(s).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private static List<String> convertToArray(String[][] permutations) {
        List<String> perms = new ArrayList<>();
        for (String[] permutation : permutations) {
            Collections.addAll(perms, permutation);
        }
        return perms;
    }

    private static List<String> returnPermutations(List<String> source, int choose, int len) {
        List<String> result = new ArrayList<>();

        if (len + 1 == choose) {
            return source;
        } else {
            for (String s : source) {
                List<String> tail = new ArrayList<>(source);
                tail.remove(s);
                result.addAll(returnPermutations(tail, choose, len + 1).stream().map(ts -> s + ts)
                        .collect(Collectors.toList()));
            }
            return result;
        }
    }


    private static String[][] permutationsViaRotation(List<String> source) {
        List<String> permuteThis = new ArrayList<>(source);
        String one = permuteThis.get(0);
        permuteThis.remove(one);
        List<String> row1 = returnPermutations(permuteThis, N - 1, 0).stream().map(ts -> one + ts)
                .collect(Collectors.toList());

        String[][] perms = new String[N][row1.size()];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < row1.size(); j++) {
                perms[i][j] = i == 0 ? row1.get(j) : rotateString(perms[i - 1][j]);
            }
        }
        return perms;
    }

    private static String rotateString(String s) {
        if (s == null || s.length() == 1) return s;
        else {
            StringBuilder sb = new StringBuilder();
            return sb.append(s.substring(1)).append(s.substring(0, 1)).toString();
        }
    }

    private static boolean isSuperPermutation(List<String> perms, String guess) {
        for (String s : perms) {
            if (!guess.contains(s)) {
                System.out.println(s);
                return false;
            }
        }
        return true;
    }
}
