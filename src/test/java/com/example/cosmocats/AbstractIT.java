package com.example.cosmocats;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

import java.util.TimeZone;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class AbstractIT {

    static final GenericContainer POSTGRES_CONTAINER = new GenericContainer("postgres:16")
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "cosmocats_db")
            .withEnv("TZ", "UTC")
            .withExposedPorts(5432);

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        POSTGRES_CONTAINER.start();
    }

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .configureStaticDsl(true)
            .build();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("application.payment-service.base-path", wireMockServer::baseUrl);
        registry.add("spring.datasource.url", () -> String.format("jdbc:postgresql://%s:%d/cosmocats_db?options=-c%%20timezone=UTC",
                POSTGRES_CONTAINER.getHost(), POSTGRES_CONTAINER.getMappedPort(5432)));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");

        WireMock.configureFor(wireMockServer.getPort());
    }
}
