/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 24/05/2026
 * Usage    : Custom validation annotation for inline actor/category DTO lists
 * Since    : Version 1.0
 */
package com.me.learning.catalog.service.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validates that inline actor/category lists do not contain null items.
 * Nested field validation is handled by {@code @Valid} on the DTO field.
 */
@Documented
@Constraint(validatedBy = ValidInlineActorsAndCategoriesValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInlineActorsAndCategories {

    String message() default "Inline objects must not contain null elements";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

