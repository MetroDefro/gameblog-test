package sparta.gameblog;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MySQLTestContainer {
    @Container
    private static final MySQLContainer MYSQL = new MySQLContainer("mysql:8")
            .withDatabaseName("blog_test")
            .withUsername("user")
            .withPassword("password");
}
