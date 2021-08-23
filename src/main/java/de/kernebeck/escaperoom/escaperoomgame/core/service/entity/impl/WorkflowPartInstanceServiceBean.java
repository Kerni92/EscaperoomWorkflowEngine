package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class WorkflowPartInstanceServiceBean implements WorkflowPartInstanceService {

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Autowired
    private RiddleInstanceService riddleInstanceService;

    @Override
    public WorkflowPartInstance findWorkflowPartInstanceById(Long id) {
        if (id != null) {
            Optional<WorkflowPartInstance> WorkflowPartInstance = workflowPartInstanceRepository.findById(id);
            if (WorkflowPartInstance.isPresent()) {
                return WorkflowPartInstance.get();
            }
        }
        return null;
    }

    @Override
    public WorkflowPartInstance createWorkflowPartInstanceFromWorkflowPart(Game game, WorkflowPart workflowPart, Timestamp start) {
        if (workflowPart != null) {
            final WorkflowPartInstance instance = workflowPartInstanceRepository.save(new WorkflowPartInstance(workflowPart, null, start, start, null, game));
            //create riddle instances
            for (final Riddle r : workflowPart.getRiddles()) {
                riddleInstanceService.createRiddleInstanceByRiddle(r, instance);
            }
            //return new loaded instance because of newly set links to riddleinstances
            return workflowPartInstanceRepository.findById(instance.getId()).get(); //could not be null
        }

        return null;
    }

    @Override
    public WorkflowPartInstance createWorkflowPartInstanceFromWorkflowPart(Game game, WorkflowPart workflowPart) {
        return this.createWorkflowPartInstanceFromWorkflowPart(game, workflowPart, null);
    }

    @Override
    public WorkflowPartInstance save(WorkflowPartInstance workflowPartInstance) {
        if (workflowPartInstance != null) {
            return workflowPartInstanceRepository.save(workflowPartInstance);
        }
        return null;
    }
}
