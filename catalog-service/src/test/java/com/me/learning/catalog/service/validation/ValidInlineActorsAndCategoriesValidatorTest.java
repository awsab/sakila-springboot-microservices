package com.me.learning.catalog.service.validation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidInlineActorsAndCategoriesValidator")
class ValidInlineActorsAndCategoriesValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    private ValidInlineActorsAndCategoriesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidInlineActorsAndCategoriesValidator();
    }

    @Test
    @DisplayName("initialize should be a no-op")
    void initializeNoOp() {
        assertDoesNotThrow(() -> validator.initialize(null));
    }

    @Test
    @DisplayName("null list should be valid")
    void nullListShouldBeValid() {
        assertThat(validator.isValid(null, context)).isTrue();
    }

    @Test
    @DisplayName("empty list should be valid")
    void emptyListShouldBeValid() {
        assertThat(validator.isValid(List.of(), context)).isTrue();
    }

    @Test
    @DisplayName("list without null items should be valid")
    void nonNullListShouldBeValid() {
        assertThat(validator.isValid(List.of("a", "b", "c"), context)).isTrue();
    }

    @Test
    @DisplayName("list containing null item should be invalid")
    void nullItemShouldBeInvalid() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(Arrays.asList("a", null, "c"), context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Inline object at index 1 cannot be null");
    }
}
