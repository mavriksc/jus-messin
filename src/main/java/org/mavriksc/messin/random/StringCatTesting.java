package org.mavriksc.messin.random;

import org.apache.commons.lang3.StringUtils;

public class StringCatTesting {
    public static void main(String[] args) {
        System.out.println(getAddressAsIdentifier("102 First St", "Apt 1", "Irving", "TX", "75063"));

    }

    private static String getAddressAsIdentifier(String street1, String street2, String city, String state, String postalCode) {
        String identifier = (StringUtils.trimToEmpty(street1) +
                (StringUtils.isBlank(street2) ? "" : street2.trim()) + StringUtils.trimToEmpty(city) +
                (state != null ? state : "")).replace(" ", "");

        if (identifier.length() >= 90) {
            identifier = identifier.substring(0, 90);
        }
        identifier += StringUtils.trimToEmpty(postalCode);

        return identifier.toLowerCase();
    }
}
