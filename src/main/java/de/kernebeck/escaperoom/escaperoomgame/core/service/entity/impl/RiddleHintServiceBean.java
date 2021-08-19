package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleHintRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RiddleHintServiceBean implements RiddleHintService {

    @Autowired
    private RiddleHintRepository riddleHintRepository;

    @Override
    public RiddleHint load(Long id) {
        return riddleHintRepository.findById(id).orElse(null);
    }

    @Override
    public List<RiddleHint> findAllRiddleHintByRiddle(Riddle riddle) {
        if (riddle != null) {
            return riddleHintRepository.findAllRiddleHintByRiddle(riddle.getId());
        }

        return Collections.emptyList();
    }

    @Override
    public List<RiddleHint> findUsedRiddleHintsForRiddleInstance(RiddleInstance riddleInstance) {
        if (riddleInstance != null) {
            return riddleHintRepository.findAllRiddleHintByRiddleInstance(riddleInstance.getId());
        }

        return Collections.emptyList();
    }
}
