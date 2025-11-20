package com.example.cosmocats.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CategoryRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 3, max = 20, message = "must be between 3 and 20 characters long")
    String name;

    @NotBlank(message = "is required")
    @Size(min = 3, max = 20, message = "must be between 3 and 20 characters long")
    @Pattern(
            regexp = "^[a-zA-Z0-9-]+$",
            message = "must contain only letters, numbers and dashes"
    )
    String slug;
}
