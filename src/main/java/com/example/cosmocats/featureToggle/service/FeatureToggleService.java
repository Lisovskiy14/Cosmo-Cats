package com.example.cosmocats.featureToggle.service;

import java.util.Map;

public interface FeatureToggleService {
    boolean isEnabled(String featureName);
    void enableFeature(String featureName);
    void disableFeature(String featureName);
}
