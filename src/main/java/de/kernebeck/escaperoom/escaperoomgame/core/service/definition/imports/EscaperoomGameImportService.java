package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;

import java.io.File;
import java.io.Reader;

public interface EscaperoomGameImportService {

    WorkflowImportResult createEscaperoomgameFromFile(File file);

    WorkflowImportResult createEscaperoomGameFromReader(Reader reader) throws Exception;


}
