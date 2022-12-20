package com.example.cinematicketbookingapp.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class OnePartSurnameValidatorTest {

    private final OnePartSurnameValidator onePartSurnameValidator = new OnePartSurnameValidator();
    private final ConstraintValidatorContext constraintValidatorContext = mock(javax.validation.ConstraintValidatorContext.class);

    @Test
    void isValid_returnsTrue_whenSurnameIsAtLeasThreeCharactersLongAndStartsWithCapitalLetter() {
        //given

        //when

        //then
        assertTrue(onePartSurnameValidator.isValid(ValidationTestConstantValues.CORRECT_ONE_PART_SURNAME, constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {ValidationTestConstantValues.ONE_PART_SURNAME_TOO_SHORT_AND_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.ONE_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.ONE_PART_SURNAME_TOO_SHORT})
    void isValid_returnsFalse_whenNameIsInvalid(String surname) {
        //given

        //when

        //then
        assertFalse(onePartSurnameValidator.isValid(surname, constraintValidatorContext));
    }

    @Test
    void isValid_returnsTrue_whenSurnameContainsDash() {
        //given

        //when

        //then
        assertTrue(onePartSurnameValidator.isValid(ValidationTestConstantValues.ANY_SURNAME_WITH_SINGLE_DASH, constraintValidatorContext));
    }
}