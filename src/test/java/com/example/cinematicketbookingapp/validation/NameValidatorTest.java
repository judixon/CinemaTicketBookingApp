package com.example.cinematicketbookingapp.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class NameValidatorTest {

    private final NameValidator nameValidator = new NameValidator();
    private final ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_returnsTrue_whenNameIsAtLeasThreeCharactersLongAndStartsWithCapitalLetter() {
        //given

        //when

        //then
        assertTrue(nameValidator.isValid(ValidationTestConstantValues.CORRECT_NAME, constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {ValidationTestConstantValues.NAME_TOO_SHORT,
            ValidationTestConstantValues.NAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.NAME_TOO_SHORT_AND_STARTS_WITH_LOWERCASE_LETTER})
    void isValid_returnsFalse_whenNameIsInvalid(String surname) {
        //given

        //when

        //then
        assertFalse(nameValidator.isValid(surname, constraintValidatorContext));
    }
}