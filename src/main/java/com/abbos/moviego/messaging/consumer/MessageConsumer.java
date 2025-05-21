package com.abbos.moviego.messaging.consumer;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import com.abbos.moviego.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(WelcomeEmailDto dto) {
        try {
            emailService.send(dto);
            log.info("Email consumed and processed: {}", dto.email());
        } catch (Exception e) {
            log.error("Failed to process email: {}", dto.email(), e);
        }
    }
}
