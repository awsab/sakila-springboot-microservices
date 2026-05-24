/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 24/05/2026
 * Usage    : Validator implementation for ValidCategoryIds annotation
 * Since    : Version 1.0
 */
package com.me.learning.catalog.service.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.me.learning.catalog.entity.Category;
import com.me.learning.catalog.repository.CategoryRepository;

/**
 * Validates category IDs using a single repository batch lookup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidCategoryIdsValidator implements ConstraintValidator<ValidCategoryIds, List<Short>> {

    private final CategoryRepository categoryRepository;

    @Override
    public void initialize(ValidCategoryIds constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(List<Short> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        int nullIndex = findNullIndex(value);
        if (nullIndex >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Category ID at index " + nullIndex + " cannot be null")
                    .addConstraintViolation();
            return false;
        }

        Set<Short> missingIds = findMissingIds(value);
        if (missingIds.isEmpty()) {
            return true;
        }

        addViolation(context, missingIds);
        log.warn("Category ID validation failed. Missing IDs: {}", missingIds);
        return false;
    }

    private int findNullIndex(List<Short> categoryIds) {
        for (int index = 0; index < categoryIds.size(); index++) {
            if (categoryIds.get(index) == null) {
                return index;
            }
        }
        return -1;
    }

    private Set<Short> findMissingIds(List<Short> categoryIds) {
        Set<Short> requestedIds = new HashSet<>(categoryIds);
        Set<Short> foundIds = categoryRepository.findAllById(requestedIds).stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        return requestedIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toSet());
    }

    private void addViolation(ConstraintValidatorContext context, Set<Short> missingIds) {
        String missingIdsText = missingIds.stream()
                .limit(5)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        if (missingIds.size() > 5) {
            missingIdsText += ", ...";
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Invalid category IDs: [" + missingIdsText + "]")
                .addConstraintViolation();
    }
}
