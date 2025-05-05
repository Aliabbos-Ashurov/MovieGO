package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Event;
import com.abbos.moviego.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN FETCH e.movie m WHERE e.cinemaHall.id = :cinemaHallId")
    List<Event> findEventsByCinemaHallId(Long cinemaHallId);

    @Query("SELECT e FROM Event e WHERE e.movie.id = :movieId")
    List<Event> findEventsByMovieId(Long movieId);

    @Query("SELECT e FROM Event e WHERE e.status = :status")
    List<Event> findEventsByStatus(EventStatus status);
}
