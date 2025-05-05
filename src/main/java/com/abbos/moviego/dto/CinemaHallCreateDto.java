package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import com.abbos.moviego.enums.CinemaHallStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record CinemaHallCreateDto(
        @NotBlank(message = "Cinema hall name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be positive")
        Integer capacity,

        @NotNull(message = "Status is required")
        CinemaHallStatus status,

        @NotNull
        MultipartFile image
) implements Request {
}
