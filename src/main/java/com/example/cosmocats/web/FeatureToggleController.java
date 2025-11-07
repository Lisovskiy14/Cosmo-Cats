package com.example.cosmocats.web;

import com.example.cosmocats.dto.featureToggle.FeatureTogglesListResponseDto;
import com.example.cosmocats.featureToggle.service.impl.FeatureToggleServiceImpl;
import com.example.cosmocats.web.mapper.FeatureToggleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feature-toggles")
public class FeatureToggleController {
    private final FeatureToggleServiceImpl featureToggleService;
    private final FeatureToggleMapper featureToggleMapper;

    @GetMapping
    public ResponseEntity<Object> getAllFeatures() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FeatureTogglesListResponseDto(
                        featureToggleService.getAllFeatures().entrySet().stream()
                                .map(featureToggleMapper::toFeatureToggleResponseDto)
                                .toList()));
    }

    @PatchMapping("/enable/{featureName}")
    public ResponseEntity<Object> enableFeature(@PathVariable String featureName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(featureToggleMapper.toFeatureToggleResponseDto(
                        featureToggleService.enableFeature(featureName)));
    }

    @PatchMapping("/disable/{featureName}")
    public ResponseEntity<Object> disableFeature(@PathVariable String featureName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(featureToggleMapper.toFeatureToggleResponseDto(
                        featureToggleService.disableFeature(featureName)));
    }
}
