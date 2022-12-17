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
        assertTrue(twoPartSurnameValidator.isValid("Aaa-Aaa", constraintValidatorContext));
    }

    @Test
    void isValid_returnsTrue_whenSurnameDoesNotContainDash() {
        //given

        //when

        //then
        assertTrue(twoPartSurnameValidator.isValid("Aaa", constraintValidatorContext));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa-Aaa", "Aa-Aaa", "aaa-Aaa", "Aaa-aa", "Aaa-Aa", "Aaa-aaa"})
    void isValid_returnsFalse_whenAtLeastOnePartOfTwoPartSurnameIsInvalid(String surname) {
        //given

        //when

        //then
        assertFalse(twoPartSurnameValidator.isValid(surname, constraintValidatorContext));
    }
}