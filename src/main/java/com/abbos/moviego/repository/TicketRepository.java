package com.abbos.moviego.repository;

import com.abbos.moviego.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t JOIN FETCH t.event e WHERE t.user.id = :userId")
    List<Ticket> findTicketsByUserId(Long userId);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.user u WHERE t.event.id = :eventId")
    Ticket findTicketsByEventId(Long eventId);
}
