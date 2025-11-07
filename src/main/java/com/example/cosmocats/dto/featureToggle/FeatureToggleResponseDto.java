package com.example.cosmocats.dto.featureToggle;

import lombok.Value;

@Value
public class FeatureToggleResponseDto {
    String featureName;
    boolean enabled;
}
