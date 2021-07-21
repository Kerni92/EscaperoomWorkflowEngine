package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;

public interface WorkflowPartInstanceService {

    WorkflowPartInstance findWorkflowPartInstanceById(Long id);

    WorkflowPartInstance createWorkflowPartInstanceFromWorkflowPart(Game game, WorkflowPart workflowPart);

    String getGameIdFromWorkflowPartInstance(WorkflowPartInstance workflowPartInstance);

}
