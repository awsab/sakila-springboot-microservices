/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 24/05/2026
 * Usage    : Validator implementation for ValidActorIds annotation
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

import com.me.learning.catalog.entity.Actor;
import com.me.learning.catalog.repository.ActorRepository;

/**
 * Validates actor IDs using a single repository batch lookup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidActorIdsValidator implements ConstraintValidator<ValidActorIds, List<Integer>> {

    private final ActorRepository actorRepository;

    @Override
    public void initialize(ValidActorIds constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        int nullIndex = findNullIndex(value);
        if (nullIndex >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Actor ID at index " + nullIndex + " cannot be null")
                    .addConstraintViolation();
            return false;
        }

        Set<Integer> missingIds = findMissingIds(value);
        if (missingIds.isEmpty()) {
            return true;
        }

        addViolation(context, missingIds, "Invalid actor IDs: [");
        log.warn("Actor ID validation failed. Missing IDs: {}", missingIds);
        return false;
    }

    private int findNullIndex(List<Integer> actorIds) {
        for (int index = 0; index < actorIds.size(); index++) {
            if (actorIds.get(index) == null) {
                return index;
            }
        }
        return -1;
    }

    private Set<Integer> findMissingIds(List<Integer> actorIds) {
        Set<Integer> requestedIds = new HashSet<>(actorIds);
        Set<Integer> foundIds = actorRepository.findAllById(requestedIds).stream()
                .map(Actor::getId)
                .collect(Collectors.toSet());
        return requestedIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toSet());
    }

    private void addViolation(ConstraintValidatorContext context, Set<Integer> missingIds, String prefix) {
        String missingIdsText = missingIds.stream()
                .limit(5)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        if (missingIds.size() > 5) {
            missingIdsText += ", ...";
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(prefix + missingIdsText + "]")
                .addConstraintViolation();
    }
}
