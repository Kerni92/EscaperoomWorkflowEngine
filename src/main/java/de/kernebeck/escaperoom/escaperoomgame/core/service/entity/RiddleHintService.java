package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

import java.util.List;

public interface RiddleHintService {

    RiddleHint load(Long id);

    List<RiddleHint> findAllRiddleHintByRiddle(Riddle riddle);

    List<RiddleHint> findUsedRiddleHintsForRiddleInstance(RiddleInstance riddleInstance);
}
