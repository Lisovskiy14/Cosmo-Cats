package com.example.cosmocats.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "application.features")
public class FeatureToggleProperties {

    Map<String, Boolean> toggles;

    public boolean checkFeature(String featureName) {
        return toggles.getOrDefault(featureName, false);
    }
}
