package com.abbos.moviego.repository;

import com.abbos.moviego.dto.MovieDetailDto;
import com.abbos.moviego.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface MovieRepository extends ListCrudRepository<Movie, Long> {

    @Query("""
                SELECT m FROM Movie m
                JOIN FETCH m.category c
                JOIN FETCH m.posterImage pi
                LEFT JOIN FETCH m.sceneImages si
                LEFT JOIN FETCH si.image img
                ORDER BY m.id DESC
            """)
    List<Movie> findAllEager();

    @Query(value = """
                SELECT * FROM movies
                ORDER BY created_at DESC
                LIMIT 1
            """, nativeQuery = true)
    Optional<Movie> findLastMovieNative();

    @Query("""
                SELECT new com.abbos.moviego.dto.MovieDetailDto(
                    m.id,
                    m.title,
                    m.trailerLink,
                    m.durationMinutes,
                    m.language,
                    m.rating,
                    m.description,
                    c.name,
                    pi.link,
                    null,
                    null
                )
                FROM Movie m
                JOIN m.category c
                JOIN m.posterImage pi
                WHERE m.id = :id
            """)
    Optional<MovieDetailDto> findMovieBaseDetails(@Param("id") Long id);

}
