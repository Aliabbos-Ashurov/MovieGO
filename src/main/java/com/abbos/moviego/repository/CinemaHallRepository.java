package com.abbos.moviego.repository;

import com.abbos.moviego.entity.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface CinemaHallRepository extends ListCrudRepository<CinemaHall, Long> {

    @Query("SELECT ch FROM CinemaHall ch WHERE ch.name = :name")
    Optional<CinemaHall> findByName(String name);
}
