package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.RiddleSolvedEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.RiddleExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiddleExecutionServiceBean implements RiddleExecutionService {

    @Autowired
    private GameService gameService;

    @Autowired
    private RiddleInstanceService riddleInstanceService;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private EventBus eventBus;

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
            riddleInstanceService.save(riddleInstance);

            if (foundCorrectSolution) {
                eventBus.post(new RiddleSolvedEvent(game.getGameId(), riddleInstance.getRiddle().getId()));
            }

            return foundCorrectSolution;
        }
        return false;
    }

}
