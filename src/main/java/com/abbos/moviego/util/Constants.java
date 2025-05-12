package com.abbos.moviego.util;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface Constants extends Util {
    String DASHBOARD_VIEW = "dashboard";
    String FRAGMENT_KEY = "contentFragment";
    String[] OPEN_PAGES = {
            "/auth/signup",
            "/auth/login",
            "/error/**",
            "/home",
            "/movies/{id}",
            "/css/**",
            "/h2-console/**",
            "/console/**"
    };
}
