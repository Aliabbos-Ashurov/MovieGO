package com.abbos.moviego.messaging.producer;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private MessageProducer messageProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(messageProducer, "exchange", "test-exchange");
        ReflectionTestUtils.setField(messageProducer, "routingKey", "test-routing-key");
    }

    @Test
    void send_shouldPublishMessage() {
        WelcomeEmailDto dto = new WelcomeEmailDto("user@example.com", "Welcome!");

        messageProducer.send(dto);

        verify(rabbitTemplate).convertAndSend("test-exchange", "test-routing-key", dto);
    }
}

