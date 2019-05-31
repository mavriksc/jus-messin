package org.mavriksc.messin.hackerrank;

        import java.util.Scanner;

public class LoopyI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int N = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 11; i++) {
            sb.append(N).append(" x ").append(i).append(" = ").append(N * i).append("\n");
        }
        System.out.println(sb.toString());


        scanner.close();
    }
}
