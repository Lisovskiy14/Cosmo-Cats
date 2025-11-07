package com.example.cosmocats.featureToggle.service.impl;

import com.example.cosmocats.config.FeatureToggleProperties;
import com.example.cosmocats.featureToggle.exception.FeatureNotFoundException;
import com.example.cosmocats.featureToggle.service.FeatureToggleService;
import org.springframework.stereotype.Service;

import java.util.Map;
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
    public Map.Entry<String, Boolean> enableFeature(String featureName) {
        checkForFeature(featureName);
        featureProps.put(featureName, true);
        return getEntryByFeatureName(featureName);
    }

    @Override
    public Map.Entry<String, Boolean> disableFeature(String featureName) {
        checkForFeature(featureName);
        featureProps.put(featureName, false);
        return getEntryByFeatureName(featureName);
    }

    @Override
    public Map<String, Boolean> getAllFeatures() {
        return featureProps;
    }

    private void checkForFeature(String featureName) {
        if (!featureProps.containsKey(featureName)) {
            throw new FeatureNotFoundException(featureName);
        }
    }


    private Map.Entry<String, Boolean> getEntryByFeatureName(String featureName) {
        return featureProps.entrySet().stream()
                .filter(entry -> entry.getKey()
                        .equals(featureName))
                .findFirst()
                .get();
    }
}
