package com.example.cinematicketbookingapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Character.isUpperCase(s.charAt(0)) && s.length()>2;
    }
}
