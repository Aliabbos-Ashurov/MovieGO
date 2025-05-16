package com.abbos.moviego;

import com.abbos.moviego.config.security.SessionUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;


@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class MovieGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieGoApplication.class, args);
	}

    @Bean
    public AuditorAware<Long> auditorAware(SessionUser sessionUser) {
        return () -> Optional.ofNullable(sessionUser.getId());
    }
}