package de.kernebeck.escaperoom.escaperoomgame;

import de.kernebeck.escaperoom.escaperoomgame.basic.database.MariaDBTestContainer;
import org.junit.ClassRule;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest(excludeAutoConfiguration = {EscaperoomgameWebappConfiguration.class, EscaperoomGameWebsecurityConfiguration.class, TestDatabaseAutoConfiguration.class})
@TestPropertySource(locations = "classpath:integrationtest_application.properties")
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static MariaDBTestContainer mariaDBTestContainer = MariaDBTestContainer.getInstance();

}
