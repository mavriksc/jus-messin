/*
* @(#)TestCurrency.java Oct 25, 2017
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author SCarlisle
 *
 */
public class TestCurrency {

    public static String commaSepratedPrice(Object price) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CANADA);
        String symbol = formatter.getCurrency().getSymbol(Locale.CANADA);
        // or "-"+symbol if that's what you need
        formatter.setNegativePrefix("-" + symbol + "\u2009");
        formatter.setPositivePrefix(symbol + "\u2009");
        formatter.setNegativeSuffix("");
        return formatter.format(price);
    }

    public static String commaSepratedPrice(Object price, String locale) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CANADA);
        Locale l;
        if (locale != null && !locale.isEmpty()) {
            String[] lSplit = locale.split("_");
            l = new Locale(lSplit[0], lSplit[1]);
        } else {
            l = Locale.CANADA;
        }

        String symbol = formatter.getCurrency().getSymbol(l);
        // or "-"+symbol if that's what you need
        formatter.setNegativePrefix("-" + symbol);
        formatter.setPositivePrefix(symbol);
        formatter.setNegativeSuffix("");
        return formatter.format(price);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Float price = 1350.849f;
        String result = commaSepratedPrice(price).split("\\.")[0];

        Money usMoney = new Money();
        usMoney.ammount = BigDecimal.valueOf(50.23);
        Currency usc = Currency.getInstance(usMoney.currencyCode);
        Money caMoney = new Money("CAD");
        caMoney.ammount = usMoney.ammount;
        Currency cac = Currency.getInstance(caMoney.currencyCode);
        
        
        String l = "en";
        String c = "CA";
        Locale loc = new Locale(l,c);
        
        System.out.println("RESULT: " + result);
        System.out.println("Original CA+CA: " + commaSepratedPrice(price));

        System.out.println("new CA+CA: " + commaSepratedPrice(price, Locale.CANADA.toString()));

        System.out.println("new CA+US: " + commaSepratedPrice(price, Locale.US.toString()));

        System.out.println("new CA+null: " + commaSepratedPrice(price, null));
        System.out.println("new CA+empty: " + commaSepratedPrice(price, ""));

        System.out.println("---------new stuff----------------------");
        System.out.println("us Symbol: " + usc.getSymbol());
        System.out.println("ca Symbol default: " + cac.getSymbol());
        System.out.println("ca Symbol: " + cac.getSymbol(Locale.CANADA));
        System.out.println("usMoney+ us Locale: " + usMoney.toString());
        System.out.println("usMoney+ ca Locale: " + usMoney.toString(Locale.CANADA));
        System.out.println("caMoney+ us Locale: " + caMoney.toString());
        System.out.println("caMoney+ ca Locale: " + caMoney.toString(Locale.CANADA));
        System.out.println("German Locale: " + Locale.GERMANY.toString());

        System.out.println("loc test: " + loc.toString());
        
        
        System.out.println("USD currency to string "+Currency.getInstance("USD").toString());

    }

}
