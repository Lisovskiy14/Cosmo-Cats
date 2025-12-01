package com.example.cosmocats.featureToggle.exception;

public class FeatureNotAvailableException extends RuntimeException {
    private static final String FEATURE_NOT_AVAILABLE = "Feature '%s' is not available";

    public FeatureNotAvailableException(String featureName) {
        super(String.format(FEATURE_NOT_AVAILABLE, featureName));
    }
}
