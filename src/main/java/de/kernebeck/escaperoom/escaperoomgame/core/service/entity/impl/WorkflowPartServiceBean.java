package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowPartRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkflowPartServiceBean implements WorkflowPartService {

    @Autowired
    private WorkflowPartRepository workflowPartRepository;

    @Override
    public WorkflowPart findWorkflowPartById(Long id) {
        if (id != null) {
            Optional<WorkflowPart> workflowPart = workflowPartRepository.findById(id);
            if (workflowPart.isPresent()) {
                return workflowPart.get();
            }
        }
        return null;
    }
}
