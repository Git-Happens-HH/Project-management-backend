package githappens.hh.project_management_app.RepositoryTests;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

abstract class AbstractPostgresBaseClass {

    static final PostgreSQLContainer<?> postgres;

    static {
        long start = System.currentTimeMillis();
        postgres = new PostgreSQLContainer<>("postgres:16-alpine");
        postgres.start();
        long end = System.currentTimeMillis();
        System.out.println("----------Kontin käynnistys kesti: " + (end - start) + " ms----------");
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
