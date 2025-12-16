package org.mavriksc.messin.random;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidationStuff {

    public static void main(String[] args) {
        ValidateMe v = new ValidateMe();
        v.setValid(" ");
        validation(v);
    }
    
    
    private static void validation (ValidateMe v) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        validator.validate(v).forEach(violation -> System.out.println(violation.getMessage()));
        System.out.println(v);
    }
}
