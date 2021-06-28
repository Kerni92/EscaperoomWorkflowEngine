package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;

import java.util.List;

public interface SolutionService {

    List<Solution> findByRiddleId(Long riddleId);

}
