package com.abbos.moviego.config;

import com.abbos.moviego.entity.User;
import com.abbos.moviego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads user details by email for Spring Security authentication.
 * Wraps {@link User} into a {@link UserPrincipal}.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        return new UserPrincipal(user);
    }
}
