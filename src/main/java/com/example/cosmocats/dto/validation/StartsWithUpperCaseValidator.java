package com.example.cosmocats.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartsWithUpperCaseValidator implements ConstraintValidator<StartsWithUpperCase, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^[A-ZА-Я].*") && !s.isEmpty();
    }
}
