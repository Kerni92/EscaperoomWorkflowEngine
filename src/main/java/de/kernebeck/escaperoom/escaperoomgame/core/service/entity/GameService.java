package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

import java.util.List;

public interface GameService {

    Game createGame(Long workflowId, List<String> usernames);

    Game findByGameId(String gameId);

    Game findGameByRiddleInstance(RiddleInstance riddleInstance);

    boolean executeWorkflowTransition(Game game, WorkflowTransition workflowTransition);

    void startGame(Game game);

    void pauseGame(Game game);

    void continueGame(Game game);

    Game findNotFinishedByGameId(String gameId);

    Game save(Game game);
}
