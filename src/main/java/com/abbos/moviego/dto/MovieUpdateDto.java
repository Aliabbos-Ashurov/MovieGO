package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record MovieUpdateDto(
        @NotNull Long id,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @NotNull(message = "Duration is required")
        @Min(1)
        Integer durationMinutes,

        @NotNull
        @NotBlank(message = "Genre is required")
        String genre,

        @NotNull
        @NotBlank(message = "Language is required")
        String language,

        @NotNull
        @NotBlank(message = "Rating is required")
        String rating,

        @NotNull
        @NotBlank(message = "Trailer link is required")
        String trailerLink,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) implements Request {
}
