package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;

import java.util.Optional;

public interface GameRepository extends BasicEntityRepository<Game> {

    Optional<Game> findByGameIdAndFinishedFalse(String gameid);

}
