package com.example.Final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FinalApplication {
	@Bean
	public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(FinalApplication.class, args);
	}

}
