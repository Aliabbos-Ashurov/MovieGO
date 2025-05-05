package com.abbos.moviego.dto;

import com.abbos.moviego.annotation.OptionalField;
import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CategoryCreateDto(
        @NotBlank(message = "Category name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @OptionalField
        String description
) implements Request {
}
