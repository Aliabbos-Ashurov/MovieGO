package com.abbos.moviego.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility component for retrieving the currently authenticated user's information
 * from the Spring Security context.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-10
 */
@Slf4j
@Component
public class SessionUser {

    /**
     * Returns the currently authenticated {@link UserPrincipal}.
     *
     * @return the authenticated user or {@code null} if not available
     */
    public UserPrincipal getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal user) {
            return user;
        }
        log.warn("::::::::: SESSION USER IS NULL :::::::::");
        return null;
    }

    /**
     * Returns the ID of the currently authenticated user.
     *
     * @return user ID or throws {@code NullPointerException} if user is not authenticated
     */
    public Long getId() {
        return getUser().getId();
    }
}
