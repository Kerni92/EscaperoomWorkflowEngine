package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartModel extends LoadableDetachableModel<WorkflowPart> {

    @SpringBean
    private WorkflowPartService workflowPartService;

    private final Long workflowPartId;

    public WorkflowPartModel(Long workflowPartId) {
        super();
        Injector.get().inject(this);
        this.workflowPartId = workflowPartId;
    }

    @Override
    protected WorkflowPart load() {
        if (workflowPartId != null) {
            return workflowPartService.findWorkflowPartById(workflowPartId);
        }
        return null;
    }
}
