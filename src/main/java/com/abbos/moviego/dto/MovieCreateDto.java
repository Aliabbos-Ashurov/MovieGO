package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record MovieCreateDto(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @NotNull(message = "Duration is required")
        @Min(1)
        Integer durationMinutes,

        @NotBlank(message = "Language is required")
        String language,

        @NotBlank(message = "Rating is required")
        String rating,

        @NotBlank(message = "Trailer link is required")
        String trailerLink,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotNull(message = "Poster image required")
        MultipartFile posterImage,

        @NotNull(message = "scene images required")
        List<MultipartFile> sceneImages
) implements Request {
}
