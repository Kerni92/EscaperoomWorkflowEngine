package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowTransitionRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WorkflowTransitionServiceBean implements WorkflowTransitionService {

    @Autowired
    private WorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Override
    public List<WorkflowTransition> findWorkflowTransitionsByWorkflowPartInstance(WorkflowPartInstance workflowPartInstance) {
        if (workflowPartInstance != null && workflowPartInstance.getRiddleInstanceList().stream().noneMatch(r -> r.isResolved() == null || !r.isResolved())) {
            final List<WorkflowTransition> transitions = workflowTransitionRepository.findByWorkflowPartInstanceAndOrderBySortIndex(workflowPartInstance.getId());
            if (!transitions.isEmpty()) {
                return new ArrayList<>(transitions);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<WorkflowTransition> findWorkflowTransitionsByWorkflowPartInstanceId(Long workflowPartInstanceId) {
        if (workflowPartInstanceId != null) {
            return findWorkflowTransitionsByWorkflowPartInstance(workflowPartInstanceService.findWorkflowPartInstanceById(workflowPartInstanceId));
        }
        return Collections.emptyList();
    }


}
