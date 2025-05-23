package com.abbos.moviego.messaging.consumer;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import com.abbos.moviego.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Test
    void testReceiveMessage_callsEmailService() {
        WelcomeEmailDto dto = new WelcomeEmailDto("user@example.com", "Welcome!");

        messageConsumer.receiveMessage(dto);

        verify(emailService).send(dto);
    }

    @Test
    void testReceiveMessage_logsErrorWhenExceptionThrown() {
        WelcomeEmailDto dto = new WelcomeEmailDto("user@example.com", "Welcome!");
        doThrow(new RuntimeException("fail")).when(emailService).send(dto);

        messageConsumer.receiveMessage(dto);

        verify(emailService).send(dto);
    }
}
