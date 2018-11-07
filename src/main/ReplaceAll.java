/*
* @(#)ReplaceAll.java Nov 7, 2017
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package main;

/**
 * @author SCarlisle
 *
 */
public class ReplaceAll {

    private static final String thisString = " <div><ul><li>The amount of any trade in credit you receive depends on the model of device, the degree and type of cosmetic damage and whether the device is in working order or not. The device must be your property.</li><li>The device may be from any carrier, but it is your responsibility to deactivate any active device before trading it in. The trade-in does not automatically stop billing for the service on an active device.</li><li>It is your responsibility to delete all your personal information from the device. The device may be reused and a subsequent user will have access to any information you leave on it.</li><li>Trade in credits can be used toward purchasing a new wireless device or accessory. Each credit is valued at $1. Trade in credits are not transferable and cannot be exchanged for cash. Productspurchased using trade in credits cannot be returned for full cash value.</li><li>All trade-ins are final. The trade in transfers all rights you have in the device and, except for the trade in credits you receive in exchange, you waive any claim you may have against TELUS and HYLA with respect to the device or the trade in.</li></ul></div>";

    public static void main(String[] args) {
        String replace = "CAD 1";
        String result = thisString.replaceAll("\\$1", replace);
        String result2 = thisString.replace("$1", replace);
        System.out.println(result);
        System.out.println(result2);

    }

}
