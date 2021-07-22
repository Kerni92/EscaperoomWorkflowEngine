package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkflowTransitionRepository extends BasicEntityRepository<WorkflowTransition> {

    @Query(value = "SELECT * FROM workflowtransition WHERE fk_sourcepart=(SELECT fk_workflowpart FROM workflowpartinstance WHERE id=:workflowPartInstanceId ORDER BY sortindex ASC);", nativeQuery = true)
    List<WorkflowTransition> findByWorkflowPartInstanceAndOrderBySortIndex(@Param("workflowPartInstanceId") Long workflowPartInstanceId);

}
