package com.example.cosmocats.common.jpa;

import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class NaturalIdGenerationListener {

    @PrePersist
    public void generateNaturalId(Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            GeneratedNaturalId annotation = field.getAnnotation(GeneratedNaturalId.class);
            if (annotation != null) {
                field.setAccessible(true);
                Object existingValue = field.get(entity);
                if (existingValue == null) {
                    String newValue = annotation.type().generate();
                    field.set(entity, newValue);
                }
            }
        });
    }
}
