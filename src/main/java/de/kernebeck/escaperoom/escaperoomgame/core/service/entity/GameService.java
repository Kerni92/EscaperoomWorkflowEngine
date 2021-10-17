package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.GameResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;

import java.util.List;

public interface GameService {

    /**
     * Erzeugt eine neue Spielinstanz für einen Escaperoom Workflow
     *
     * @param workflowId Id des Workflow für den eine Spielinstanz erzeugt werden soll
     * @param usernames  Spielernamen, die am Spiel teilnehmen wollen
     * @return Instanz des Objekts Game wenn die Aktion erfolgreich war, sonst NULL
     */
    Game createGame(Long workflowId, List<String> usernames);

    /**
     * Berechnet das Ergebnisobjektes für die Ergebnisseite am Ende eines Spiels
     *
     * @param gameId - Technische Id des Spiels für den die Informationen berechnet werden sollen
     * @return Objekt mit den berechneten Informationen
     */
    GameResultDTO calculateGameResultInformationForGame(Long gameId);

    /**
     * Lade Spiel anhand der kommunizierten Spiel ID
     *
     * @param gameId Id des Spiels - Achtung NICHT TECHNISCH
     * @return Spiel falls eins gefunden wurde, sonst NULL
     */
    Game findByGameId(String gameId);


    Game findGameByRiddleInstance(RiddleInstance riddleInstance);

    Game findNotFinishedByGameId(String gameId);

    List<Game> findRunningGames();

    /**
     * Lade Spiel anhand der technischen ID
     *
     * @param id technische ID des Spiels
     * @return Spiel falls gefunden sonst NULL
     */
    Game load(Long id);

    Game save(Game game);
}
