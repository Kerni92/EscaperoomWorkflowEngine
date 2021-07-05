package de.kernebeck.escaperoom.escaperoomgame.core.service.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;

public interface WorkflowExecutionService {

    boolean isTransitionExecutionPossible(Game game, WorkflowTransition workflowTransition);

    WorkflowPartInstance executeWorkflowTransition(Game game, WorkflowTransition workflowTransition);

}
