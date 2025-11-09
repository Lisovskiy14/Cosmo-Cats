package com.example.cosmocats.featureToggle.service.impl;

import com.example.cosmocats.config.FeatureToggleProperties;
import com.example.cosmocats.featureToggle.service.FeatureToggleService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureToggleServiceImpl implements FeatureToggleService {
    private final ConcurrentHashMap<String, Boolean> featureProps;

    public FeatureToggleServiceImpl(FeatureToggleProperties featureProps) {
        this.featureProps = new ConcurrentHashMap<>(featureProps.getToggles());
    }

    @Override
    public boolean isEnabled(String featureName) {
        return featureProps.getOrDefault(featureName, false);
    }

    @Override
    public void enableFeature(String featureName) {
        featureProps.put(featureName, true);
    }

    @Override
    public void disableFeature(String featureName) {
        featureProps.put(featureName, false);
    }
}
