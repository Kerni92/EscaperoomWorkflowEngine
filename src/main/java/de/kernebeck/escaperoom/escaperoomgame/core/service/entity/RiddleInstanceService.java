package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;

public interface RiddleInstanceService {

    RiddleInstance findById(Long id);

    RiddleInstance createRiddleInstanceByRiddle(Riddle riddle, WorkflowPartInstance workflowPartInstance);

    boolean checkSolution(RiddleInstance riddleInstance, String solution);

}
