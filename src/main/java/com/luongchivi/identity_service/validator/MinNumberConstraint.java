package com.luongchivi.identity_service.validator;

import com.luongchivi.identity_service.validator.handle.MinNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {MinNumberValidator.class})
public @interface MinNumberConstraint {
    String message() default "Invalid filed must be greater than one";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
