package de.kernebeck.escaperoom.escaperoomgame.core.service.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

public interface RiddleExecutionService {

    boolean checkSolution(RiddleInstance riddleInstance, String solution);

    RiddleHint getNextRiddleHint(RiddleInstance riddleInstance);
}
