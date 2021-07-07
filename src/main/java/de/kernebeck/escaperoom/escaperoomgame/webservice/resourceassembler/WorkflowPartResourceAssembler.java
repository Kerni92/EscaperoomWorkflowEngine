package de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.RiddleInstanceResource;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.WorkflowPartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkflowPartResourceAssembler {

    @Autowired
    private RiddleInstanceResourceAssembler riddleResourceAssembler;

    public WorkflowPartResource convertEntityToResource(WorkflowPartInstance wp) {
        final List<RiddleInstanceResource> riddles = new ArrayList<>();

        for (RiddleInstance r : wp.getRiddleInstanceList()) {
            riddles.add(riddleResourceAssembler.convertEntityToResource(r));
        }

        return new WorkflowPartResource(wp.getWorkflowPart().getId(), wp.getWorkflowPart().getName(), wp.getWorkflowPart().getDescription(), riddles);
    }

}
