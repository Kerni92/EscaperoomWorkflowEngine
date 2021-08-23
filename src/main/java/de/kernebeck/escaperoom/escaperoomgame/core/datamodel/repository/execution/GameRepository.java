package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends BasicEntityRepository<Game> {

    Optional<Game> findByGameId(String gameid);

    Optional<Game> findByGameIdAndFinishedFalse(String gameid);

    @Query(value = "SELECT * FROM game AS g WHERE g.id =(SELECT w.fk_game FROM workflowpartinstance AS w WHERE w.id = (SELECT r.fk_workflowpartinstance FROM riddleinstance AS r WHERE r.id =:riddleInstanceId))", nativeQuery = true)
    Optional<Game> findGameByRiddleInstance(@Param("riddleInstanceId") Long riddleInstanceId);

    List<Game> findGamesByFinishedFalse();

}
