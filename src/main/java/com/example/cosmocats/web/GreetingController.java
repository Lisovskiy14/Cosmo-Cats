package com.example.cosmocats.web;

import com.example.cosmocats.featureToggle.FeatureToggles;
import com.example.cosmocats.featureToggle.annotation.FeatureToggle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greeting")
public class GreetingController {

    @FeatureToggle(FeatureToggles.GREETING)
    @RequestMapping("{name}")
    public ResponseEntity<Object> greeting(@PathVariable String name) {
        return ResponseEntity.ok()
                .body("Hello and welcome, " + name + "!");
    }
}
