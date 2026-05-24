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

import com.me.learning.catalog.entity.Category;
import com.me.learning.catalog.repository.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidCategoryIdsValidator")
class ValidCategoryIdsValidatorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    private ValidCategoryIdsValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidCategoryIdsValidator(categoryRepository);
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
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("empty list should be valid")
    void emptyListShouldBeValid() {
        assertThat(validator.isValid(List.of(), context)).isTrue();
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("list with null id should be invalid")
    void nullIdShouldBeInvalid() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(Arrays.asList((short) 1, null, (short) 2), context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Category ID at index 1 cannot be null");
        verify(categoryRepository, never()).findAllById(anySet());
    }

    @Test
    @DisplayName("all existing category IDs should be valid")
    void allExistingIdsShouldBeValid() {
        Category category1 = new Category();
        category1.setId((short) 1);
        Category category2 = new Category();
        category2.setId((short) 2);

        when(categoryRepository.findAllById(anySet())).thenReturn(List.of(category1, category2));

        assertThat(validator.isValid(List.of((short) 1, (short) 2), context)).isTrue();

        verify(categoryRepository).findAllById(anySet());
    }

    @Test
    @DisplayName("missing category ID should be invalid")
    void missingIdsShouldBeInvalid() {
        Category category1 = new Category();
        category1.setId((short) 1);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);
        when(categoryRepository.findAllById(anySet())).thenReturn(List.of(category1));

        assertThat(validator.isValid(List.of((short) 1, (short) 99), context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Invalid category IDs: [99]");
    }
}
