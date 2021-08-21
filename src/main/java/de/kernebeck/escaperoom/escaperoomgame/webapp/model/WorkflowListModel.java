package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class WorkflowListModel extends AbstractEscaperoomLoadableDetachableModel<List<Workflow>> {

    @SpringBean
    private transient WorkflowService workflowService;

    public WorkflowListModel() {
        super();
        Injector.get().inject(this);
    }

    @Override
    protected List<Workflow> loadInternal() {
        return workflowService.findAll();
    }
}
