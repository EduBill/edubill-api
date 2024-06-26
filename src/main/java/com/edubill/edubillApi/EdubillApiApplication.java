package com.edubill.edubillApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing을 활성화
public class EdubillApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdubillApiApplication.class, args);
	}

}
