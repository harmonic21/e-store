package ru.simple.electronic.store.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestConfiguration extends AbstractR2dbcConfiguration {

    static {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
        redis.start();
        System.setProperty("redis.hostname", redis.getHost());
        System.setProperty("redis.port", redis.getMappedPort(6379).toString());
    }

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.3")
                .withReuse(true)
                .withDatabaseName("yandex-test-db")
                .withInitScript("schema.sql");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        PostgreSQLContainer<?> postgreSQLContainer = postgreSQLContainer();
        return ConnectionFactoryBuilder.withUrl("r2dbc:postgresql://%s:%s/%s".formatted(postgreSQLContainer.getHost(), postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName()))
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
}
