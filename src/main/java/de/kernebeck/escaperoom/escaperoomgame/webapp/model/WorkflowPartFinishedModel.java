package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartFinishedModel extends LoadableDetachableModel<Boolean> {

    @SpringBean
    private WorkflowPartInstanceService workflowPartInstanceService;

    private Long workflowPartInstanceId = null;

    public WorkflowPartFinishedModel() {
        super();
        Injector.get().inject(this);
    }

    public WorkflowPartFinishedModel(Long workflowPartInstanceId) {
        super();
        Injector.get().inject(this);
        this.workflowPartInstanceId = workflowPartInstanceId;
    }

    @Override
    protected Boolean load() {
        if (workflowPartInstanceId != null) {
            final WorkflowPartInstance instance = workflowPartInstanceService.findWorkflowPartInstanceById(workflowPartInstanceId);
            if (instance != null) {
                return instance.getRiddleInstanceList().isEmpty() || instance.getRiddleInstanceList().stream().noneMatch(r -> r == null || !r.isResolved());
            }
        }
        return Boolean.FALSE;
    }
}
