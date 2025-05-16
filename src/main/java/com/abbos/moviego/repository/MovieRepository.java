package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

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
            """)
    List<Movie> findAllEager();
}
