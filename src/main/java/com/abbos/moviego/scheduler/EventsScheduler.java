package com.abbos.moviego.scheduler;

import com.abbos.moviego.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler component responsible for updating the status of events to COMPLETED
 * once their showtime has passed.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventsScheduler implements Scheduler {

    private final EventService eventService;

    @Scheduled(cron = "0 0 * * * *")
    public void updateCompletedEvents() {
        int updatedCount = eventService.markCompletedEvents();
        log.info("Marked {} events as COMPLETED", updatedCount);
    }
}
