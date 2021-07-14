package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class WorkflowListModel extends LoadableDetachableModel<List<Workflow>> {

    private WorkflowService workflowService;

    public WorkflowListModel(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Override
    protected List<Workflow> load() {
        return workflowService.findAll();
    }
}
