/*
* @(#)FlatMapT.java Mar 22, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin.random;

import org.mavriksc.messin.objects.InnerThing;
import org.mavriksc.messin.objects.MiddleThing;
import org.mavriksc.messin.objects.OuterThing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SCarlisle
 *
 */
public class FlatMapT {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<InnerThing> iList = new ArrayList<>();

        InnerThing it = new InnerThing();
        it.setData("");
        iList.add(it);
        it = new InnerThing();
        it.setData("");
        iList.add(it);
        List<MiddleThing> mList = new ArrayList<>();
        MiddleThing mt = new MiddleThing();
        mt.setIt(iList);
        mList.add(mt);
        mt = new MiddleThing();
        mList.add(mt);
        OuterThing ot = new OuterThing();
        ot.setMt(mList);
        System.out.println(getData(ot));
    }

    private static String getData(OuterThing ot) {
        if (ot != null && ot.getMt() != null) {
            return ot.getMt().stream().flatMap(mt -> mt.getIt() != null ? mt.getIt().stream() : null)
                    .map(InnerThing::getData).filter(it -> it != null && it.compareTo("") != 0).findFirst()
                    .orElse("NOT FOUND");
        }
        return "NONE";
    }
}
