package com.abbos.moviego.dto;

import com.abbos.moviego.dto.base.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public record UserUpdateDto(
        @NotNull
        Long id,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "old Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String oldPassword,

        @NotBlank(message = "new Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) implements Request {
}
