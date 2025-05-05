package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record MovieResponseDto(
        Long id,
        String title,
        Integer durationMinutes,
        String genre,
        String language,
        String rating,
        String trailerLink,
        String description,
        CategoryResponseDto category,
        ImageResponseDto posterImage,
        List<SceneImageResponseDto> sceneImages,
        LocalDateTime createdAt
) implements Response {
}
