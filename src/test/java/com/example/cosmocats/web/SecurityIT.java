package com.example.cosmocats.web;


import com.example.cosmocats.AbstractIT;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Security IT")
@Tag("security")
public class SecurityIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.security.api-key}")
    private String apiKey;

    @Test
    @SneakyThrows
    @DisplayName("Should Return 401 Unauthorized Exception")
    public void shouldReturn401Unauthorized() {
        mockMvc.perform(get("/api/v1/categories")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.details").value("API key is missing"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return 200 OK")
    public void shouldReturn200OK() {
        mockMvc.perform(get("/api/v1/categories")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray());
    }
}
