package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartInstanceModel extends LoadableDetachableModel<WorkflowPartInstance> {

    @SpringBean
    private WorkflowPartInstanceService workflowPartInstanceService;

    private Long workflowPartId;

    public WorkflowPartInstanceModel() {
        //required for serialization and deserialization
        super();
        Injector.get().inject(this);

    }

    public WorkflowPartInstanceModel(Long workflowPartId) {
        super();
        Injector.get().inject(this);
        this.workflowPartId = workflowPartId;
    }

    @Override
    protected WorkflowPartInstance load() {
        if (workflowPartId != null) {
            return workflowPartInstanceService.findWorkflowPartInstanceById(workflowPartId);
        }
        return null;
    }
}
