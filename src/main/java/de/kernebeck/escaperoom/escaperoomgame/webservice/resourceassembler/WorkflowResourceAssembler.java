package de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.WorkflowResource;
import org.springframework.stereotype.Component;

@Component
public class WorkflowResourceAssembler {


    public WorkflowResource convertEntityToResource(Workflow wf) {
        return new WorkflowResource(wf.getId(), wf.getDbcreationdate(), wf.getDbupdatedate(), wf.getName(), wf.getWorkflowStart().getId());
    }

}
