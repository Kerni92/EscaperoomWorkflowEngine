package de.kernebeck.escaperoom.escaperoomgame.core.service.entity;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;

import java.util.List;
import java.util.Optional;

public interface WorkflowService {

    public List<Workflow> findAll();

    public Optional<Workflow> findById(Long id);

}
