package com.abbos.moviego.messaging.producer;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public void send(WelcomeEmailDto dto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
        log.info("Email message published to exchange [{}] with routing key [{}]", exchange, routingKey);
    }
}
