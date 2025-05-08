package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CategoryUpdateDto(
        @NotNull
        Long id,

        @NotBlank
        @Size(max = 50)
        @Column(nullable = false, unique = true)
        String name,

        String description
) implements Request {
}
