package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowTransitionService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowTransitionModel extends AbstractEscaperoomLoadableDetachableModel<WorkflowTransition> {

    @SpringBean
    private transient WorkflowTransitionService workflowTransitionService;

    private Long id = null;

    public WorkflowTransitionModel() {
        super();
        Injector.get().inject(this);
        //default constructor required for serialization
    }

    public WorkflowTransitionModel(WorkflowTransition object) {
        super(object);
        Injector.get().inject(this);
        if (object != null) {
            this.id = object.getId();
        }
    }

    public WorkflowTransitionModel(Long id) {
        super();
        Injector.get().inject(this);
        this.id = id;
    }

    @Override
    protected WorkflowTransition loadInternal() {
        if (id != null) {
            return workflowTransitionService.findById(id);
        }
        return null;
    }
}
