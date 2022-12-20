package com.example.cinematicketbookingapp.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class TwoPartSurnameValidatorTest {

    private final TwoPartSurnameValidator twoPartSurnameValidator = new TwoPartSurnameValidator();
    private final ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_returnsTrue_whenBothPartsStartsWithCapitalLetterAndAreAtLeasThreeCharactersLong() {
        //given

        //when

        //then
        assertTrue(twoPartSurnameValidator.isValid(ValidationTestConstantValues.CORRECT_TWO_PART_SURNAME, constraintValidatorContext));
    }

    @Test
    void isValid_returnsTrue_whenSurnameDoesNotContainDash() {
        //given

        //when

        //then
        assertTrue(twoPartSurnameValidator.isValid(ValidationTestConstantValues.CORRECT_ONE_PART_SURNAME, constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {ValidationTestConstantValues.TOO_SHORT_FIRST_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.FIRST_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.TOO_SHORT_FIRST_PART_OF_TWO_PART_SURNAME,
            ValidationTestConstantValues.SECOND_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.TOO_SHORT_SECOND_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER,
            ValidationTestConstantValues.TOO_SHORT_SECOND_PART_OF_TWO_PART_SURNAME,
            ValidationTestConstantValues.BOTH_PARTS_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER_AND_ARE_TOO_SHORT})
    void isValid_returnsFalse_whenAtLeastOnePartOfTwoPartSurnameIsInvalid(String surname) {
        //given

        //when

        //then
        assertFalse(twoPartSurnameValidator.isValid(surname, constraintValidatorContext));
    }
}