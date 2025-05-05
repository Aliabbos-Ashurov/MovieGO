package com.abbos.moviego.util;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-02
 */
public interface Constants extends Util {
    String BASE_PATH_V1 = ""; // must be:    /api/v1/   in REST
    String AUTH_TYPE = "Bearer ";
    String[] OPEN_PAGES = {
            "/auth/**",
            "/static/**",
            "/h2-console/**",
            "/console/**"
    };
}
