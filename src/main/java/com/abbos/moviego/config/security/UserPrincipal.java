package com.abbos.moviego.config.security;

import com.abbos.moviego.entity.Role;
import com.abbos.moviego.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom {@link UserDetails} implementation that wraps the application's {@link User} entity.
 * Provides user identity and authorities for Spring Security authentication and authorization.
 * Extracts roles and permissions into a flat set of granted authorities.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
@Getter
public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final User user;
    private final String email;
    private final Collection<GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.user = user;
        this.id = user.getId();
        this.email = user.getEmail();
        this.authorities = buildAuthorities(user.getRoles());
    }

    private Collection<GrantedAuthority> buildAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
            role.getPermissions().forEach(
                    permission -> authorities.add(new SimpleGrantedAuthority(permission.getName().toUpperCase()))
            );
        }
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
