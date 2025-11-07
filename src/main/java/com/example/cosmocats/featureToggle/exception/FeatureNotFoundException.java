package com.example.cosmocats.featureToggle.exception;

public class FeatureNotFoundException extends RuntimeException {
    private static final String FEATURE_NOT_FOUND = "Feature '%s' not found";

    public FeatureNotFoundException(String featureName) {
        super(String.format(FEATURE_NOT_FOUND, featureName));
    }
}
