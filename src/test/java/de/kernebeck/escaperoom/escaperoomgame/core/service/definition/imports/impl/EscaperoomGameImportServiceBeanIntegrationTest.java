package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation.User;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.authorisation.UserRespository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class EscaperoomGameImportServiceBeanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRespository userRespository;

    @Test
    public void testCreateUser() {
        userRespository.save(new User("blub", "blub", "blub", "blub", true));
        assertThat(userRespository.findUserByUsername("blub")).isNotEmpty();
    }


}