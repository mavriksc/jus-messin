/*
* @(#)Sorter.java Apr 13, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SCarlisle
 *
 */
public class Sorter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String[]> stuff = new ArrayList<>();
        stuff.sort((sa1,sa2)->Integer.parseInt(sa1[0])-Integer.parseInt(sa2[0]));

    }

}
