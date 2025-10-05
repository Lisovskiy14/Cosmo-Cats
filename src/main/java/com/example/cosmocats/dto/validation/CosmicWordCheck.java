package com.example.cosmocats.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CosmicWordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "Field must contain at least one cosmic word (e.g. 'cosmo', 'galaxy', 'star', 'comet')";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
