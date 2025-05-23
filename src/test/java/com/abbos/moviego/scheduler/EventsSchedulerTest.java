package com.abbos.moviego.scheduler;

import com.abbos.moviego.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
public class EventsSchedulerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventsScheduler eventsScheduler;

    @Test
    void updateCompletedEvents_callsEventService_andLogs() {
        when(eventService.markCompletedEvents()).thenReturn(5);
        eventsScheduler.updateCompletedEvents();
        verify(eventService, times(1)).markCompletedEvents();
    }
}
