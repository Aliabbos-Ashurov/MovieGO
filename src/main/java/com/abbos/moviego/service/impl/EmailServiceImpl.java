package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import com.abbos.moviego.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void send(WelcomeEmailDto dto) {
        Context context = new Context();
        context.setVariable("fullname", dto.fullname());

        System.out.println(Thread.currentThread().getName());

        String htmlContent = templateEngine.process("render/welcome", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.email());
            helper.setSubject("Welcome to MovieGO!");
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Welcome email sent to {}", dto.email());
        } catch (MessagingException e) {
            log.error("Error sending email to {}", dto.email(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
