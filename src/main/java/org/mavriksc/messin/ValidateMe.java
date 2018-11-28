
package org.mavriksc.messin;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

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
