package de.kernebeck.escaperoom.escaperoomgame.core.service.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

public interface GameExecutionService {

    boolean executeWorkflowTransition(Game game, WorkflowTransition workflowTransition);

    boolean checkRiddleSolution(Long gameId, RiddleInstance riddleInstance, String solution);

    void startGame(Game game);

    void pauseGame(Game game);

    void continueGame(Game game);

    void finishGame(Game game);
}
