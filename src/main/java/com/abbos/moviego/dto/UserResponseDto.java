package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record UserResponseDto(
        Long id,
        String fullname,
        String email,
        ImageResponseDto profilePic,
        List<RoleResponseDto> roles,
        LocalDateTime createdAt
) implements Response {
}
