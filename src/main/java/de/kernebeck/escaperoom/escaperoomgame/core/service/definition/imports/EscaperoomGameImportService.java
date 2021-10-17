package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;

import java.io.File;
import java.io.Reader;

public interface EscaperoomGameImportService {

    /**
     * Erzeugt einen neuen Workflow anhand eines definierten JSON-Schemas
     *
     * @param file - JSON-Datei mit den Importdaten
     * @return WorkflowImportResult Objekt welches behinhaltet ob der Import erfolgreich war und falls nicht welche Fehler aufgetreten sind
     */
    WorkflowImportResult createEscaperoomgameFromFile(File file);

    /**
     * Erzeugt einen neuen Workflow anhand eines definierten JSON-Schemas
     *
     * @param reader - Reader der einen Stringinhalt repräsentieren muss
     * @return WorkflowImportResult Objekt welches behinhaltet ob der Import erfolgreich war und falls nicht welche Fehler aufgetreten sind
     */
    WorkflowImportResult createEscaperoomGameFromReader(Reader reader) throws Exception;


}
