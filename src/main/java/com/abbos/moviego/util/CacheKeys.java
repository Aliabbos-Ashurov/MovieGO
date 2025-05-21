package com.abbos.moviego.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheKeys implements Util {

    public static final String FIND_ALL = "'findAll'";

    public static final class UserKeys {
        public static final String USER = "user";
        public static final String USERS = "users";
        public static final String USER_ME = "user-me";
    }

    public static final class RoleKeys {
        public static final String ROLE = "role";
        public static final String ROLES = "roles";
    }

    public static final class PermissionKeys {
        public static final String PERMISSION = "permission";
        public static final String PERMISSIONS = "permissions";
    }

    public static final class MovieKeys {
        public static final String MOVIE = "movie";
        public static final String MOVIES = "movies";
        public static final String MOVIE_DETAIL = "movie-detail";
    }

    public static final class EventKeys {
        public static final String EVENT = "event";
        public static final String EVENTS = "events";
    }

    public static final class CinemaHallKeys {
        public static final String CINEMA_HALL = "cinema-hall";
        public static final String CINEMA_HALLS = "cinema-halls";
    }

    public static final class CategoryKeys {
        public static final String CATEGORY = "category";
        public static final String CATEGORIES = "categories";
    }
}
