package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;

import java.util.List;

public interface WorkflowRepository extends BasicEntityRepository<Workflow> {

    List<Workflow> findByName(String name);

}
