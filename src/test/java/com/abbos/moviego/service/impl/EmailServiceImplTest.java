package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.internal.WelcomeEmailDto;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    void send_shouldPrepareAndSendEmail() {
        WelcomeEmailDto dto = new WelcomeEmailDto("test@abbos.com", "Abbos");

        when(templateEngine.process(eq("render/welcome"), any(Context.class)))
                .thenReturn("<html><body>Welcome, Abbos</body></html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(dto);

        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq("render/welcome"), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }
}
