package com.example.cinematicketbookingapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TwoPartSurnameValidator.class)
public @interface TwoPartSurname {
    String message() default "String should start with capital letter.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
