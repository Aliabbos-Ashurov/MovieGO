package com.abbos.moviego.dto.auth;

import com.abbos.moviego.dto.base.Request;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public record SignUpDto(
        @Column(name = "fullname", nullable = false)
        String fullname,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) implements Request {

}
