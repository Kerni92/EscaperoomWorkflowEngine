package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;

import java.util.List;

public interface WorkflowTransitionService {

    WorkflowTransition findById(Long id);

    List<WorkflowTransition> findWorkflowTransitionsByWorkflowPartInstance(WorkflowPartInstance workflowPartInstance);

    List<WorkflowTransition> findWorkflowTransitionsByWorkflowPartInstanceId(Long workflowPartInstanceId);
}
