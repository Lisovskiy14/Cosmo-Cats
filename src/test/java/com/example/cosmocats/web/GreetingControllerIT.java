package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.featureToggle.FeatureToggleExtension;
import com.example.cosmocats.featureToggle.FeatureToggles;
import com.example.cosmocats.featureToggle.annotation.DisabledFeatureToggle;
import com.example.cosmocats.featureToggle.annotation.EnabledFeatureToggle;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GreetingController IT")
@ExtendWith(FeatureToggleExtension.class)
public class GreetingControllerIT extends AbstractIT {

    private final String name = "User";

    @Autowired
    private MockMvc mockMvc;

    @EnabledFeatureToggle(FeatureToggles.GREETING)
    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    public void handleGreetingWithEnabledFeatureToggle() {
        mockMvc.perform(get("/api/v1/greeting/" + name))
                .andExpect(status().isOk());
    }

    @DisabledFeatureToggle(FeatureToggles.GREETING)
    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    public void handleGreetingWithDisabledFeatureToggle() {
        mockMvc.perform(get("/api/v1/greeting/" + name))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:problem-type:feature-not-found"))
                .andExpect(jsonPath("$.title").value("Feature Not Available Exception"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/greeting/" + name));
    }
}
