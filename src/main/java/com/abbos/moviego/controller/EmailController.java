package com.abbos.moviego.controller;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import com.abbos.moviego.messaging.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final MessageProducer producer;

    @PostMapping("/mail")
    public void email(@RequestBody WelcomeEmailDto dto) {
        producer.send(dto);
    }
}
