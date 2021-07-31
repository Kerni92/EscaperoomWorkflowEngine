package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.RiddleSolvedEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RiddleInstanceServiceBean implements RiddleInstanceService {

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private GameService gameService;

    @Autowired
    private EventBus eventBus;

    @Override
    public RiddleInstance findById(Long id) {
        final Optional<RiddleInstance> riddle = riddleInstanceRepository.findById(id);
        return riddle.orElse(null);
    }

    @Override
    public RiddleInstance createRiddleInstanceByRiddle(Riddle riddle, WorkflowPartInstance workflowPartInstance) {
        if (riddle != null && workflowPartInstance != null) {
            final RiddleInstance instance = new RiddleInstance(riddle, Collections.emptyList(), workflowPartInstance, 0, Boolean.FALSE);
            return riddleInstanceRepository.save(instance);
        }
        return null;
    }

    @Override
    public boolean checkSolution(RiddleInstance riddleInstance, String solution) {
        if (riddleInstance != null) {
            final Game game = gameService.findGameByRiddleInstance(riddleInstance);
            if (game == null) {
                return false;
            }
            final List<Solution> riddleSolutions = solutionService.findByRiddleId(riddleInstance.getRiddle().getId());
            final boolean foundCorrectSolution = riddleSolutions.stream().anyMatch(s -> s.getSolution().equals(solution));
            riddleInstance.setAttempts(riddleInstance.getAttempts() != null ? (riddleInstance.getAttempts() + 1) : 1);
            if (foundCorrectSolution) {
                riddleInstance.setResolved(Boolean.TRUE);
            }
            riddleInstanceRepository.save(riddleInstance);

            if (foundCorrectSolution) {
                eventBus.post(new RiddleSolvedEvent(game.getGameId(), riddleInstance.getRiddle().getId()));
            }

            return foundCorrectSolution;
        }
        return false;
    }
}
