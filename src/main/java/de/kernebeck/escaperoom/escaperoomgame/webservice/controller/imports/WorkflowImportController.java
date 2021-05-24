package de.kernebeck.escaperoom.escaperoomgame.webservice.controller.imports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Reader;
import java.util.Collections;

@RestController
@RequestMapping(path = "/api/import")
public class WorkflowImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowImportController.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EscaperoomGameImportService escaperoomGameImportService;

    @Autowired
    public WorkflowImportController(EscaperoomGameImportService escaperoomGameImportService) {
        this.escaperoomGameImportService = escaperoomGameImportService;
    }

    @PostMapping(path = "createNewWorkflow", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewWorkflow(Reader contentReader) {
        try {
            final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomGameFromReader(contentReader);
            final String value = OBJECT_MAPPER.writeValueAsString(result);

            if (result.isSuccess()) {
                return ResponseEntity.ok(value);
            }
            return ResponseEntity.badRequest().body(value);
        }
        catch (Exception e) {
            //catch every exception to avoid unexpected internal server errors
            LOGGER.error("Unexpected error while creation new workflow!", e);
            try {
                return ResponseEntity.badRequest().body(OBJECT_MAPPER.writeValueAsString(new WorkflowImportResult(false, Collections.singletonList("Unerwarteter Fehler bei der Durchführung! Meldung: " + e.getMessage()))));
            }
            catch (JsonProcessingException jsonProcessingException) {
                return ResponseEntity.badRequest().body("Unerwarteter Fehler aufgetreten: " + e.getMessage());
            }
        }
    }
}
