package com.example.cinematicketbookingapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnePartSurnameValidator.class)
public @interface OnePartSurname {
    String message() default "String should start with capital letter.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
