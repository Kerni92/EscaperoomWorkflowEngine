package de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.WorkflowTransitionResource;
import org.springframework.stereotype.Component;

@Component
public class WorkflowTransitionResourceAssembler {

    public WorkflowTransitionResource convertEntityToResource(WorkflowTransition t) {
        return new WorkflowTransitionResource(t.getId(), t.getName(), t.getDescription(), t.getSortIndex());
    }
}
