package ru.simple.electronic.store.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestConfiguration extends AbstractR2dbcConfiguration {

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
//        PostgresqlConnectionFactory postgresqlConnectionFactory = new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host(postgreSQLContainer.getHost())
//                        .port(postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT))
//                        .username(postgreSQLContainer.getUsername())
//                        .password(postgreSQLContainer.getPassword())
//                        .database(postgreSQLContainer.getDatabaseName())
//                        .build()
//        );
        return ConnectionFactoryBuilder.withUrl("r2dbc:postgresql://%s:%s/%s".formatted(postgreSQLContainer.getHost(), postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName()))
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
}
