package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.GameResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

import java.util.List;

public interface GameService {

    Game createGame(Long workflowId, List<String> usernames);

    GameResultDTO calculateGameResultInformationForGame(Long gameId);

    Game findByGameId(String gameId);

    Game findGameByRiddleInstance(RiddleInstance riddleInstance);

    Game findNotFinishedByGameId(String gameId);

    List<Game> findRunningGames();

    Game load(Long id);

    Game save(Game game);
}
