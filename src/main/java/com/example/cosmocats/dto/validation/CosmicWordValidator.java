package com.example.cosmocats.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {
    private final Set<String> COSMIC_WORDS = Set.of("cosmo", "galaxy", "star", "comet");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) {
            return true;
        }

        String lowerCase = s.toLowerCase();
        return COSMIC_WORDS.stream().anyMatch(lowerCase::contains);
    }
}
