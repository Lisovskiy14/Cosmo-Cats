package com.example.cosmocats.featureToggle;

import com.example.cosmocats.featureToggle.annotation.DisabledFeatureToggle;
import com.example.cosmocats.featureToggle.annotation.EnabledFeatureToggle;
import com.example.cosmocats.featureToggle.service.FeatureToggleService;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureToggleExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            FeatureToggleService featureToggleService = getFeatureToggleService(context);

            if (method.isAnnotationPresent(EnabledFeatureToggle.class)) {
                EnabledFeatureToggle enabledFeatureToggle = method.getAnnotation(EnabledFeatureToggle.class);
                featureToggleService.enableFeature(enabledFeatureToggle.value().getFeatureName());
            } else if (method.isAnnotationPresent(DisabledFeatureToggle.class)) {
                DisabledFeatureToggle disabledFeatureToggle = method.getAnnotation(DisabledFeatureToggle.class);
                featureToggleService.disableFeature(disabledFeatureToggle.value().getFeatureName());
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            FeatureToggleService featureToggleService = getFeatureToggleService(context);

            if (method.isAnnotationPresent(EnabledFeatureToggle.class)) {
                EnabledFeatureToggle enabledFeatureToggle = method.getAnnotation(EnabledFeatureToggle.class);
                featureToggleService.disableFeature(enabledFeatureToggle.value().getFeatureName());
            } else if (method.isAnnotationPresent(DisabledFeatureToggle.class)) {
                DisabledFeatureToggle disabledFeatureToggle = method.getAnnotation(DisabledFeatureToggle.class);
                featureToggleService.enableFeature(disabledFeatureToggle.value().getFeatureName());
            }
        });
    }

    private FeatureToggleService getFeatureToggleService(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(FeatureToggleService.class);
    }
}
