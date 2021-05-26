package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;

import java.util.List;

public interface GameService {

    Game createGame(Long workflowId, List<String> usernames);

}
