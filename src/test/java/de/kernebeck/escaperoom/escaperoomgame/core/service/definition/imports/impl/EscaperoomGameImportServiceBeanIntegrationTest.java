package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class EscaperoomGameImportServiceBeanIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private EscaperoomGameImportService escaperoomGameImportService;

    @Test
    public void testImportSuccess() throws URISyntaxException {
        final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomgameFromFile(new File(EscaperoomGameImportService.class.getResource("importtest/game_success.json").toURI()));
        assertThat(result.isSuccess()).isTrue();
    }


}