package de.kernebeck.escaperoom.escaperoomgame.webapp.component;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class WorkflowPartComponent extends GenericPanel<WorkflowPart> {

    public WorkflowPartComponent(String id, IModel<WorkflowPart> model) {
        super(id, model);
    }


}
