package com.devlukas.hotelreservationsystem.testcontainers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
@Import({TestcontainersPropertySourceAutoConfiguration.class})
public class PostgresTestContainerConfig {

    @ServiceConnection
    @Bean
    PostgreSQLContainer postgreSQLContainer(DynamicPropertyRegistry properties) {
        var db = new PostgreSQLContainer("postgres:16-alpine");

        properties.add("spring.datasource.url", db::getJdbcUrl);
        properties.add("spring.datasource.username", db::getUsername);
        properties.add("spring.datasource.password", db::getPassword);

        return db;
    }
}
