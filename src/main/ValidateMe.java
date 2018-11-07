/*
* @(#)ValidateMe.java Feb 21, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package main;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author SCarlisle
 *
 */
public class ValidateMe {
        @NotEmpty
        @Pattern(regexp = "^$|^[\\p{L}'-]*[\\p{L}]+[\\p{L} '-]*$")
        private String valid;

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {            
            this.valid = valid!=null?valid.trim():"";
        }

        @Override
        public String toString() {
            return valid;
        }
        
        
        
}
