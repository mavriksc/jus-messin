/*
* @(#)EvenSplit.java Mar 26, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

/**
 * @author SCarlisle
 *
 */
public class EvenSplit {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        splitPrinter("HACKERS2");
        splitPrinter2("HACKERS2");
    }

    /**
     * @param s
     */
    private static void splitPrinter2(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {
                sb.insert(i, s.charAt(i));
            } else {
                sb.insert(i, " ");
                sb.append(" ");
                sb.append(s.charAt(i));
            }
        }
        System.out.println(sb.toString().trim());
    }

    private static void splitPrinter(String s) {
        StringBuilder sO = new StringBuilder();
        StringBuilder sE = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {
                sE.append(s.charAt(i));
                sE.append(" ");
            } else {
                sO.append(s.charAt(i));
                sO.append(" ");
            }
        }
        sE.append(sO.toString().trim());
        System.out.println(sE.toString());
    }

}
