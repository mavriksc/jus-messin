/*
* @(#)OuterThing.java Mar 22, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin.objects;

import java.util.List;

/**
 * @author SCarlisle
 *
 */
public class OuterThing {
    private List<MiddleThing> mt;

    public List<MiddleThing> getMt() {
        return mt;
    }

    public void setMt(List<MiddleThing> mt) {
        this.mt = mt;
    }

}
