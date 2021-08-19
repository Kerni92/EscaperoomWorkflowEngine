package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.RiddleSolvedEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.RiddleExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class RiddleExecutionServiceBean implements RiddleExecutionService {

    @Autowired
    private GameService gameService;

    @Autowired
    private RiddleInstanceService riddleInstanceService;

    @Autowired
    private RiddleHintService riddleHintService;

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

    @Override
    @Transactional
    public RiddleHint getNextRiddleHint(RiddleInstance riddleInstance) {
        if (riddleInstance != null) {
            final List<RiddleHint> usedHints = riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstance);
            final List<RiddleHint> allHints = riddleHintService.findAllRiddleHintByRiddle(riddleInstance.getRiddle());
            usedHints.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
            allHints.sort(Comparator.comparingInt(RiddleHint::getSortIndex));

            RiddleHint hintToReturn;
            if (usedHints.isEmpty()) {
                hintToReturn = allHints.get(0);
            }
            else if (usedHints.size() == allHints.size()) {
                hintToReturn = null;
            }
            else {
                hintToReturn = null;
                for (int i = 0; i < allHints.size(); i++) {
                    final RiddleHint hint = allHints.get(i);

                    if (usedHints.stream().map(RiddleHint::getId).noneMatch(id -> Objects.equals(id, hint.getId()))) {
                        hintToReturn = hint;
                        break;
                    }
                }
            }

            if (hintToReturn != null) {
                final RiddleInstance newLoadInstance = riddleInstanceService.findById(riddleInstance.getId());
                newLoadInstance.addUsedHint(hintToReturn);
                riddleInstanceService.save(newLoadInstance);
            }

            return hintToReturn;
        }
        return null;
    }

}
