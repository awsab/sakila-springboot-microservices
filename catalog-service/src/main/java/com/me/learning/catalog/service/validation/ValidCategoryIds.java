/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 24/05/2026
 * Usage    : Custom validation annotation for validating category IDs exist in the catalog database
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
import jakarta.validation.ReportAsSingleViolation;

/**
 * Validates that all category IDs in a list exist.
 */
@Documented
@Constraint(validatedBy = ValidCategoryIdsValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ValidCategoryIds {

    String message() default "One or more category IDs do not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

