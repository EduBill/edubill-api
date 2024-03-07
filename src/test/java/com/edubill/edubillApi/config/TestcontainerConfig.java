package com.edubill.edubillApi.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration(initializers = {TestcontainerConfig.Initializer.class})
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.properties"})
public @interface TestcontainerConfig {

    @Container
    MariaDBContainer mariaDBContainer = new MariaDBContainer<>("mariadb:11.2.2")
            .withDatabaseName("edubill")
            .withUsername("root")
            .withPassword("");

    class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            mariaDBContainer.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + mariaDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mariaDBContainer.getUsername(),
                    "spring.datasource.password=" + mariaDBContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
