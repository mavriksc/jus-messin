/*
* @(#)MiddleThing.java Mar 22, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package objects;

import java.util.List;

/**
 * @author SCarlisle
 *
 */
public class MiddleThing {
    private List<InnerThing> it;

    public List<InnerThing> getIt() {
        return it;
    }

    public void setIt(List<InnerThing> it) {
        this.it = it;
    }

}
