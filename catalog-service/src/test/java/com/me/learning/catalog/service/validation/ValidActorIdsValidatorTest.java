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

import com.me.learning.catalog.entity.Actor;
import com.me.learning.catalog.repository.ActorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidActorIdsValidator")
class ValidActorIdsValidatorTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    private ValidActorIdsValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidActorIdsValidator(actorRepository);
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
        verifyNoInteractions(actorRepository);
    }

    @Test
    @DisplayName("empty list should be valid")
    void emptyListShouldBeValid() {
        assertThat(validator.isValid(List.of(), context)).isTrue();
        verifyNoInteractions(actorRepository);
    }

    @Test
    @DisplayName("list with null id should be invalid")
    void nullIdShouldBeInvalid() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);

        assertThat(validator.isValid(Arrays.asList(1, null, 2), context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Actor ID at index 1 cannot be null");
        verify(actorRepository, never()).findAllById(org.mockito.ArgumentMatchers.anySet());
    }

    @Test
    @DisplayName("all existing actor IDs should be valid")
    void allExistingIdsShouldBeValid() {
        Actor actor1 = new Actor();
        actor1.setId(1);
        Actor actor2 = new Actor();
        actor2.setId(2);

        when(actorRepository.findAllById(org.mockito.ArgumentMatchers.anySet()))
                .thenReturn(List.of(actor1, actor2));

        assertThat(validator.isValid(List.of(1, 2), context)).isTrue();

        verify(actorRepository).findAllById(org.mockito.ArgumentMatchers.anySet());
    }

    @Test
    @DisplayName("missing actor ID should be invalid")
    void missingIdsShouldBeInvalid() {
        Actor actor1 = new Actor();
        actor1.setId(1);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);
        when(actorRepository.findAllById(org.mockito.ArgumentMatchers.anySet()))
                .thenReturn(List.of(actor1));

        assertThat(validator.isValid(List.of(1, 99), context)).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Invalid actor IDs: [99]");
    }
}
