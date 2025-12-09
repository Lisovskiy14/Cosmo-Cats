package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.dto.category.CategoryRequestDto;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.web.mapper.CategoryWebMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("CategoryController IT")
@Tag("category-service")
public class CategoryControllerIT extends AbstractIT {
    private static final Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private CategoryService categoryService;

    @Autowired
    private CategoryWebMapper categoryWebMapper;

    @BeforeEach
    public void setup() {
        reset(categoryService);
    }

    private static Stream<CategoryRequestDto> provideCategoryRequests() {
        return Stream.of(
                CategoryRequestDto.builder()
                        .name(faker.numerify("Category ###"))
                        .slug(faker.letterify("????"))
                        .build(),
                CategoryRequestDto.builder()
                        .name(faker.numerify("Category ###"))
                        .slug(faker.letterify("????"))
                        .build(),
                CategoryRequestDto.builder()
                        .name(faker.numerify("Category ###"))
                        .slug(faker.letterify("????"))
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategoryRequests")
    @SneakyThrows
    @DisplayName("Should Save Category")
    public void shouldSaveCategory(CategoryRequestDto categoryRequestDto) {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(categoryRequestDto.getName()))
                .andExpect(jsonPath("$.slug").value(categoryRequestDto.getSlug()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Get All Categories")
    public void shouldGetAllCategories() {
        provideCategoryRequests()
                .map((categoryRequestDto -> categoryWebMapper.toCategory(categoryRequestDto)))
                .forEach(categoryService::saveCategory);

        mockMvc.perform(get("/api/v1/categories")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories").isNotEmpty())
                .andExpect(jsonPath("$.categories[*].id").exists())
                .andExpect(jsonPath("$.categories[*].name").exists())
                .andExpect(jsonPath("$.categories[*].slug").exists());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return 404 Not Found")
    public void shouldReturn404NotFound() {
        UUID wrongId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/categories/{id}", wrongId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:problem-type:not-found"))
                .andExpect(jsonPath("$.title").value("Resource Not Found Exception"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value(String.format("Category with id '%s' not found", wrongId)))
                .andExpect(jsonPath("$.instance").value("/api/v1/categories/" + wrongId));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Get Category By Id")
    public void shouldGetCategoryById() {
        CategoryRequestDto categoryRequestDto = provideCategoryRequests().findFirst().get();
        UUID id = categoryService.saveCategory(categoryWebMapper.toCategory(categoryRequestDto)).getId();

        mockMvc.perform(get("/api/v1/categories/{id}", id)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(categoryRequestDto.getName()))
                .andExpect(jsonPath("$.slug").value(categoryRequestDto.getSlug()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Delete Category")
    public void shouldDeleteCategory() {
        mockMvc.perform(delete("/api/v1/categories/{id}", UUID.randomUUID())
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

}
