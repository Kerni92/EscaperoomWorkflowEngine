package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowTransitionService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public class ValidWorkflowTransitionListModel extends LoadableDetachableModel<List<WorkflowTransition>> {

    @SpringBean
    private WorkflowTransitionService workflowTransitionService;

    private Long workflowPartInstanceId = null;

    public ValidWorkflowTransitionListModel() {
        super();
        Injector.get().inject(this);
    }

    public ValidWorkflowTransitionListModel(Long workflowPartInstanceId) {
        super();
        Injector.get().inject(this);
        this.workflowPartInstanceId = workflowPartInstanceId;
    }

    @Override
    protected List<WorkflowTransition> load() {
        if (workflowPartInstanceId != null) {
            return workflowTransitionService.findWorkflowTransitionsByWorkflowPartInstanceId(workflowPartInstanceId);
        }
        return Collections.emptyList();
    }
}
