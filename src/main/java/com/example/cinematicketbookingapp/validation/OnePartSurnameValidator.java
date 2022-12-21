package com.example.cinematicketbookingapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnePartSurnameValidator implements ConstraintValidator<OnePartSurname, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.contains("-")) {
            return true;
        } else {
            return Character.isUpperCase(s.charAt(0)) && s.length() > 2;
        }
    }
}
