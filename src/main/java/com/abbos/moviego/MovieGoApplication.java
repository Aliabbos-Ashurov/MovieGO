package com.abbos.moviego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MovieGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieGoApplication.class, args);
	}
}