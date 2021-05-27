package de.kernebeck.escaperoom.escaperoomgame.webservice.controller.imports;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.util.Collections;

@RestController
@RequestMapping(path = "/api/import")
public class WorkflowImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowImportController.class);

    private final EscaperoomGameImportService escaperoomGameImportService;

    @Autowired
    public WorkflowImportController(EscaperoomGameImportService escaperoomGameImportService) {
        this.escaperoomGameImportService = escaperoomGameImportService;
    }

    @PostMapping(path = "/createNewWorkflow", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity createNewWorkflow(@RequestParam("file") MultipartFile file) {
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("import_", ".json");
            final FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            IOUtils.copy(file.getInputStream(), fileOutputStream);
            fileOutputStream.close();

            final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomgameFromFile(tmpFile);

            if (result.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
            return ResponseEntity.badRequest().body(result);
        }
        catch (Exception e) {
            //catch every exception to avoid unexpected internal server errors
            LOGGER.error("Unexpected error while creation new workflow!", e);
            return ResponseEntity.badRequest().body(new WorkflowImportResult(false, Collections.singletonList("Unerwarteter Fehler bei der Durchführung! Meldung: " + e.getMessage())));
        }
        finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }

    }

    @PostMapping(path = "/createNewWorkflow", consumes = "application/json", produces = "application/json")
    public ResponseEntity createNewWorkflow(Reader contentReader) {
        try {
            final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomGameFromReader(contentReader);
            if (result.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
            return ResponseEntity.badRequest().body(result);
        }
        catch (Exception e) {
            //catch every exception to avoid unexpected internal server errors
            LOGGER.error("Unexpected error while creation new workflow!", e);
            return ResponseEntity.badRequest().body(new WorkflowImportResult(false, Collections.singletonList("Unerwarteter Fehler bei der Durchführung! Meldung: " + e.getMessage())));
        }
    }
}
