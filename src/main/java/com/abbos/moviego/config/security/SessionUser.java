package com.abbos.moviego.config.security;

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

    public Long getId() {
        UserPrincipal user = getUser();
        if (user == null) {
            throw new IllegalStateException("::::::::: NO AUTHENTICATED USER FOUND :::::::::");
        }
        return user.getId();
    }
}
