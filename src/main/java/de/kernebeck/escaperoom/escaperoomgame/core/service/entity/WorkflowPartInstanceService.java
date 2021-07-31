package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;

import java.sql.Timestamp;

public interface WorkflowPartInstanceService {

    WorkflowPartInstance findWorkflowPartInstanceById(Long id);

    WorkflowPartInstance createWorkflowPartInstanceFromWorkflowPart(Game game, WorkflowPart workflowPart, Timestamp start);

    WorkflowPartInstance createWorkflowPartInstanceFromWorkflowPart(Game game, WorkflowPart workflowPart);

    WorkflowPartInstance save(WorkflowPartInstance workflowPartInstance);

}
