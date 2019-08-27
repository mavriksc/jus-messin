package org.mavriksc.messin.hackerrank;

public class Kangaroos {

    static String kangaroo(int x1, int v1, int x2, int v2) {
        if (x1 < x2 && v1 < v2)
            return "NO";
        if (x2 < x1 && v2 < v1)
            return "NO";
        if (v1 == v2)
            if (x1 != x2)
                return "NO";
            else
                return "YES";

        int dist = x1 - x2;
        boolean roo1Leads = dist >= 0;
        boolean notYetIntersected = true;
        int jumps = 0;
        int pos1, pos2;
        do {
            pos1 = v1 * jumps + x1;
            pos2 = v2 * jumps + x2;
            if (pos1 == pos2)
                return "YES";
            notYetIntersected = roo1Leads ? pos2 > pos1 : pos1 < pos2;
            jumps++;
        } while (notYetIntersected);
        return "NO";
    }
    public static void main(String[] args){
        System.out.println(kangaroo(0,3,4,2));
    }

}
