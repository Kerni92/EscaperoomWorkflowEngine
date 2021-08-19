package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiddleHintRepository extends BasicEntityRepository<RiddleHint> {


    @Query(value = "select * from riddlehint where id in(select fk_riddlehint from riddleinstance_riddlehint where fk_riddleinstance=:riddleInstanceId);", nativeQuery = true)
    List<RiddleHint> findAllRiddleHintByRiddleInstance(@Param("riddleInstanceId") Long riddleInstanceId);

    @Query(value = "select * from riddlehint where fk_riddle=:riddleId", nativeQuery = true)
    List<RiddleHint> findAllRiddleHintByRiddle(@Param("riddleId") Long riddleId);
}
