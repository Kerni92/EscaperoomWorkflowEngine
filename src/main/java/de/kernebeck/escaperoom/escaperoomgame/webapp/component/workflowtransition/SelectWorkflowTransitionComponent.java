package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class SelectWorkflowTransitionComponent extends GenericPanel<List<WorkflowTransition>> {

    public SelectWorkflowTransitionComponent(String id, IModel<List<WorkflowTransition>> model) {
        super(id, model);
    }
}
