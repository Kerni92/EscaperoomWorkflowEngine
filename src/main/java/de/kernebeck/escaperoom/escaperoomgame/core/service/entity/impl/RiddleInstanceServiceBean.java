package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class RiddleInstanceServiceBean implements RiddleInstanceService {

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;

    @Override
    public RiddleInstance findById(Long id) {
        final Optional<RiddleInstance> riddle = riddleInstanceRepository.findById(id);
        return riddle.orElse(null);
    }

    @Override
    public RiddleInstance save(RiddleInstance instance) {
        if (instance != null) {
            return riddleInstanceRepository.save(instance);
        }
        return null;
    }

    @Override
    public RiddleInstance createRiddleInstanceByRiddle(Riddle riddle, WorkflowPartInstance workflowPartInstance) {
        if (riddle != null && workflowPartInstance != null) {
            final RiddleInstance instance = new RiddleInstance(riddle, Collections.emptyList(), workflowPartInstance, 0, Boolean.FALSE);
            return riddleInstanceRepository.save(instance);
        }
        return null;
    }


}
