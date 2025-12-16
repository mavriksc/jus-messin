/*
* @(#)Money.java Feb 7, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package org.mavriksc.messin.random;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author SCarlisle
 *
 */
public class Money{
    public BigDecimal ammount = BigDecimal.ZERO;
    public String currencyCode = "USD";
    Money(){
        super();
    }
    Money(String currencyCode){
        super();
        this.currencyCode = currencyCode;
    }
    
    public String toString() {
        return toString(Locale.getDefault());
    }
    public String toString(Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setCurrency(Currency.getInstance(this.currencyCode));       
        return nf.format(this.ammount);
    }
}
