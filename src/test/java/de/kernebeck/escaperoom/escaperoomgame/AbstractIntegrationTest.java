package de.kernebeck.escaperoom.escaperoomgame;


import de.kernebeck.escaperoom.escaperoomgame.basic.database.MariaDBTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = EscaperoomgameTestApplication.class)
@TestPropertySource(locations = "classpath:integrationtest_application.properties")
public class AbstractIntegrationTest {

    public static MariaDBTestContainer mariaDBTestContainer = MariaDBTestContainer.getInstance();

}
