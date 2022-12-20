package com.example.cinematicketbookingapp.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ValidationTestConstantValues {

    static final String CORRECT_TWO_PART_SURNAME = "Aaa-Aaa";
    static final String CORRECT_ONE_PART_SURNAME = "Aaa";

    static final String TOO_SHORT_FIRST_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER = "aa-Aaa";
    static final String FIRST_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER = "aaa-Aaa";
    static final String TOO_SHORT_FIRST_PART_OF_TWO_PART_SURNAME = "Aa-Aaa";
    static final String SECOND_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER = "Aaa-aaa";
    static final String TOO_SHORT_SECOND_PART_OF_TWO_PART_SURNAME = "Aaa-Aa";
    static final String TOO_SHORT_SECOND_PART_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER = "Aaa-aa";
    static final String BOTH_PARTS_OF_TWO_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER_AND_ARE_TOO_SHORT = "aa-aa";

    static final String ONE_PART_SURNAME_TOO_SHORT_AND_STARTS_WITH_LOWERCASE_LETTER = "aa";
    static final String ONE_PART_SURNAME_STARTS_WITH_LOWERCASE_LETTER = "aaa";
    static final String ONE_PART_SURNAME_TOO_SHORT = "Aa";

    static final String NAME_TOO_SHORT_AND_STARTS_WITH_LOWERCASE_LETTER = "aa";
    static final String NAME_STARTS_WITH_LOWERCASE_LETTER = "aaa";
    static final String NAME_TOO_SHORT = "Aa";

    static final String ANY_SURNAME_WITH_SINGLE_DASH = "A-";
    static final String CORRECT_NAME = "Aaa";
}
