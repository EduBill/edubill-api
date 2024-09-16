package com.edubill.edubillApi.integration;

import com.edubill.edubillApi.config.TestcontainerConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestcontainerConfig
public class IntegrationTest {

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
        // RestAssured 포트 설정
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }
}
