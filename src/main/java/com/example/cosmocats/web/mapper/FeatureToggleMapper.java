package com.example.cosmocats.web.mapper;

import com.example.cosmocats.dto.featureToggle.FeatureToggleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface FeatureToggleMapper {
    @Mapping(source = "key", target = "featureName")
    @Mapping(source = "value", target = "enabled")
    FeatureToggleResponseDto toFeatureToggleResponseDto(Map.Entry<String, Boolean> entry);
}
