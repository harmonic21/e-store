package ru.simple.electronic.store.base;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface PostgreTestContainer {
    String DATA_BASE_NAME = "yandex-test-db";

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withReuse(true)
            .withDatabaseName(DATA_BASE_NAME)
            .withInitScript("schema.sql");
}
