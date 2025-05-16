package com.abbos.moviego.config.security;

import com.abbos.moviego.entity.Role;
import com.abbos.moviego.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
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
    private static final long serialVersionUID = -54983245224242231L;

    private final Long id;
    private final User user;
    private final String email;

    public UserPrincipal(User user) {
        this.user = user;
        this.id = user.getId();
        this.email = user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
            role.getPermissions().forEach(permission ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName().toUpperCase()))
            );
        }

        return grantedAuthorities;
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
