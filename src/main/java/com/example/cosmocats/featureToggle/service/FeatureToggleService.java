package com.example.cosmocats.featureToggle.service;

import java.util.Map;

public interface FeatureToggleService {
    boolean isEnabled(String featureName);
    Map.Entry<String, Boolean> enableFeature(String featureName);
    Map.Entry<String, Boolean> disableFeature(String featureName);
    Map<String, Boolean> getAllFeatures();
}
