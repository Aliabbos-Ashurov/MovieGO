package com.abbos.moviego.repository;

import com.abbos.moviego.dto.MovieDetailDto;
import com.abbos.moviego.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
                WHERE (:id IS NULL OR m.id = :id)
            """)
    List<MovieDetailDto> findMovieBaseDetails(@Param("id") Long id, Pageable pageable);
}
