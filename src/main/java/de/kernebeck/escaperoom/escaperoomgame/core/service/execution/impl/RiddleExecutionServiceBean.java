package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
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

    @Override
    public boolean checkSolution(RiddleInstance riddleInstance, String solution) {
        if (riddleInstance != null) {
            final Game game = gameService.findGameByRiddleInstance(riddleInstance);
            if (game == null) {
                return false;
            }
            final List<Solution> riddleSolutions = solutionService.findByRiddleId(riddleInstance.getRiddle().getId());
            final boolean foundCorrectSolution = riddleSolutions.stream().anyMatch(s -> Objects.equals(s.getSolution(), solution)); //nullsafe
            riddleInstance.setAttempts(riddleInstance.getAttempts() != null ? (riddleInstance.getAttempts() + 1) : 1);
            if (foundCorrectSolution) {
                riddleInstance.setResolved(Boolean.TRUE);
            }
            riddleInstanceService.save(riddleInstance);

            return foundCorrectSolution;
        }
        return false;
    }

    @Override
    @Transactional
    public RiddleHint getNextRiddleHint(RiddleInstance riddleInstance) {
        if (riddleInstance != null) {
            final List<RiddleHint> allHints = riddleHintService.findAllRiddleHintByRiddle(riddleInstance.getRiddle());
            if (!allHints.isEmpty()) {
                final List<RiddleHint> usedHints = riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstance);
                //we always have to order all used and all possible hints to assure that we get always the correct nextone
                usedHints.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
                allHints.sort(Comparator.comparingInt(RiddleHint::getSortIndex));

                RiddleHint hintToReturn;
                //if usedhints is empty -> its the first ordered hint so we could return the first
                if (usedHints.isEmpty()) {
                    hintToReturn = allHints.get(0);
                }
                //when usedHints size and allhints size is equal -> all hints are shown so we can return
                else if (usedHints.size() == allHints.size()) {
                    hintToReturn = null;
                }
                else {
                    //otherwise search for next valid hint to show
                    hintToReturn = null;
                    for (int i = 0; i < allHints.size(); i++) {
                        final RiddleHint hint = allHints.get(i);

                        if (usedHints.stream().map(RiddleHint::getId).noneMatch(id -> Objects.equals(id, hint.getId()))) {
                            hintToReturn = hint;
                            break;
                        }
                    }
                }

                //if a hint is found which could be shown -> save that users got the hint
                if (hintToReturn != null) {
                    final RiddleInstance newLoadInstance = riddleInstanceService.findById(riddleInstance.getId());
                    newLoadInstance.addUsedHint(hintToReturn);
                    riddleInstanceService.save(newLoadInstance);
                }

                return hintToReturn;
            }
        }
        return null;
    }

}
