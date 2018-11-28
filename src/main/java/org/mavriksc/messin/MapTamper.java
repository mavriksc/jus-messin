/*
* @(#)MapTamper.java Feb 28, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SCarlisle
 *
 */
public class MapTamper {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Thing t = new Thing();
        Map<String, Thing> m = new HashMap<>();
        m.put("thing", t);
        t.setAttribute("setAfterPut");
        t.setAttributeId(999L);
        
        m.forEach((k,v)->{
            System.out.println("attribute: "+m.get(k).getAttribute());
            System.out.println("attribute ID: "+m.get(k).getAttributeId());
        });

    }

}
