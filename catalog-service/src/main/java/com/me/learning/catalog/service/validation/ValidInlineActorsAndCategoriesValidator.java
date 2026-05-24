/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 24/05/2026
 * Usage    : Validator implementation for ValidInlineActorsAndCategories annotation
 * Since    : Version 1.0
 */
package com.me.learning.catalog.service.validation;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Validates inline actor/category lists by rejecting null elements.
 */
@Slf4j
@Component
public class ValidInlineActorsAndCategoriesValidator
        implements ConstraintValidator<ValidInlineActorsAndCategories, List<?>> {

    @Override
    public void initialize(ValidInlineActorsAndCategories constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        int nullIndex = findNullIndex(value);
        if (nullIndex < 0) {
            return true;
        }

        log.warn("Inline DTO list contains a null element at index {}", nullIndex);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Inline object at index " + nullIndex + " cannot be null")
                .addConstraintViolation();
        return false;
    }

    private int findNullIndex(List<?> value) {
        for (int index = 0; index < value.size(); index++) {
            if (value.get(index) == null) {
                return index;
            }
        }
        return -1;
    }
}

