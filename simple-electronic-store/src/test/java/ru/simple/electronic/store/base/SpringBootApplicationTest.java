package ru.simple.electronic.store.base;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {SpringBootApplicationTest.Initializer.class})
public class SpringBootApplicationTest implements PostgreTestContainer {

    static {
        postgreSQLContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "CONTAINER.USERNAME=" + postgreSQLContainer.getUsername(),
                    "CONTAINER.PASSWORD=" + postgreSQLContainer.getPassword(),
                    "CONTAINER.URL=" + r2dbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }

        private static String r2dbcUrl() {
            return "r2dbc:postgresql://%s:%s/%s".formatted(postgreSQLContainer.getHost(), postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName());
        }
    }
}
