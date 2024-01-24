package com.edubill.edubillApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing을 활성
public class EdubillApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdubillApiApplication.class, args);
	}

}
