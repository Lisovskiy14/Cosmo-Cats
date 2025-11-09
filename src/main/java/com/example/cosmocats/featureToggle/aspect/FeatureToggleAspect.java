package com.example.cosmocats.featureToggle.aspect;

import com.example.cosmocats.featureToggle.service.FeatureToggleService;
import com.example.cosmocats.featureToggle.service.impl.FeatureToggleServiceImpl;
import com.example.cosmocats.featureToggle.annotation.FeatureToggle;
import com.example.cosmocats.featureToggle.exception.FeatureNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {
    private final FeatureToggleService featureToggleService;

    @Around("@annotation(featureToggle)")
    public Object featureToggle(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        return isFeatureEnabled(joinPoint, featureToggle);
    }

    private Object isFeatureEnabled(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        String featureName = featureToggle.value().getFeatureName();
        if (featureToggleService.isEnabled(featureName)) {
            return joinPoint.proceed();
        }

        log.warn("Feature {} is disabled", featureName);
        throw new FeatureNotAvailableException(featureName);
    }
}
