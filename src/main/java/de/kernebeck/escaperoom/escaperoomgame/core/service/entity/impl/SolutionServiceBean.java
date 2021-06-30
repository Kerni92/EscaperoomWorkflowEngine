package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.SolutionRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SolutionServiceBean implements SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private RiddleService riddleService;

    @Override
    public List<Solution> findByRiddleId(Long riddleId) {
        final Riddle r = riddleService.findById(riddleId);
        if (r != null) {
            List<Solution> result = solutionRepository.findByRiddle(r);
            return result != null ? result : Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
