package com.example.cosmocats.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartsWithUpperCaseValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartsWithUpperCase {
    String message() default "Field must start with upper case letter.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
