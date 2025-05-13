package com.abbos.moviego.config;

import com.abbos.moviego.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    private final UserPrincipalService userPrincipalService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.userDetailsService(userPrincipalService);
        http.authorizeHttpRequests(httpRequet ->
                httpRequet.requestMatchers(Constants.OPEN_PAGES).permitAll()
                        .anyRequest().authenticated()
        );
        http.formLogin(httpSecurityFormLoginConfigurer ->
                httpSecurityFormLoginConfigurer
                        .loginPage("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(customAuthenticationFailureHandler)

        );
        http.logout(httpSecurityLogoutConfigurer -> {
            httpSecurityLogoutConfigurer
                    .clearAuthentication(true)
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/home")
                    .deleteCookies("remember-me", "JSESSIONID")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));
        });
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
            httpSecurityRememberMeConfigurer
                    .key(rememberMeKey)
                    .tokenValiditySeconds(1209600)
                    .rememberMeParameter("rememberMe")
                    .rememberMeCookieName("remember-me")
                    .userDetailsService(userPrincipalService)
                    .useSecureCookie(true)
                    .alwaysRemember(false);
        });
        return http.build();
    }

    /**
     * Provides a BCrypt-based password encoder for hashing user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Enables HTTP methods like PUT and DELETE via hidden form fields
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
