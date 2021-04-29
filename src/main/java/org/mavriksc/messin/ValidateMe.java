
package org.mavriksc.messin;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

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
