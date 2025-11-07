package com.example.cosmocats.dto.featureToggle;

import lombok.Value;

import java.util.List;

@Value
public class FeatureTogglesListResponseDto {
    List<FeatureToggleResponseDto> featureToggles;
}
