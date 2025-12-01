package com.example.cosmocats.featureToggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    GREETING("greeting");

    private final String featureName;

    FeatureToggles(String featureName) {
        this.featureName = featureName;
    }
}
