package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.authorisation;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserRespositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRespository userRespository;

    @BeforeEach
    public void setup() {
        userRespository.saveAndFlush(new User("admin", "admin", "admin", "tollespasswort", Boolean.TRUE));
    }

    @Test
    void testFindUserByUsername() {
        assertThat(userRespository.findUserByUsername("blub")).isEmpty();
        assertThat(userRespository.findUserByUsername("admin")).isNotEmpty();
    }

    @Test
    void testFindUserByUsernameAndPassword() {
        assertThat(userRespository.findUserByUsernameAndPassword("admin", "falschesPasswort")).isEmpty();
        assertThat(userRespository.findUserByUsernameAndPassword("admin", "tollespasswort")).isNotEmpty();
    }

    @AfterEach
    public void tearDown() {
        userRespository.deleteAll();
    }
}