package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;

import java.util.List;

public interface SolutionRepository extends BasicEntityRepository<Solution> {

    List<Solution> findByRiddle(Riddle riddle);

}
