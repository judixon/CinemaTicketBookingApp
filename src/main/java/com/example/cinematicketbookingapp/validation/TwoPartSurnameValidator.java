package com.example.cinematicketbookingapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TwoPartSurnameValidator implements ConstraintValidator<TwoPartSurname, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.contains("-")) {
            String firstPart = s.substring(0, s.indexOf("-"));
            String secondPart = s.substring(s.indexOf("-") + 1);
            return Character.isUpperCase(firstPart.charAt(0)) && Character.isUpperCase(secondPart.charAt(0)) &&
                    firstPart.length() > 2 && secondPart.length() > 2;
        }
        return true;
    }
}
