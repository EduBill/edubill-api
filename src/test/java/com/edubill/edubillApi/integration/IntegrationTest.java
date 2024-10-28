package com.edubill.edubillApi.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DatabaseCleanUp.class)
@ActiveProfiles("test")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    private static final String ROOT = "root";
    private static final String ROOT_PASSWORD = "";

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Container
    protected static MariaDBContainer container;

    @DynamicPropertySource
    private static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", () -> ROOT);
        registry.add("spring.datasource.password", () -> ROOT_PASSWORD);
    }

    static {
        container = new MariaDBContainer("mariadb:11.2.2")
                .withDatabaseName("edubill")
                .withUsername(ROOT)
                .withPassword(ROOT_PASSWORD);
        container.start();
    }


    @BeforeEach
    void setUp() {
        // RestAssured 포트 설정
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }

        databaseCleanUp.clear();

    }
}

