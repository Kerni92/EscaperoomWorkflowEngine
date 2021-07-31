package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.RiddleInstanceModel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public class WorkflowPartInstanceComponent extends GenericPanel<WorkflowPartInstance> {

    private IModel<Boolean> workflowPartFinishedModel;

    public WorkflowPartInstanceComponent(String id, IModel<WorkflowPartInstance> model, IModel<Boolean> workflowPartFinishedModel) {
        super(id, model);
        this.workflowPartFinishedModel = workflowPartFinishedModel;
        final WorkflowPartInstance workflowPartInstance = getModelObject();
        final RepeatingView riddles = new RepeatingView("riddles");
        if (workflowPartInstance.getRiddleInstanceList() != null && !workflowPartInstance.getRiddleInstanceList().isEmpty()) {
            for (RiddleInstance ri : workflowPartInstance.getRiddleInstanceList()) {
                riddles.add(new RiddleComponent(riddles.newChildId(), new RiddleInstanceModel(ri.getId())));
            }
        }
        add(riddles);
    }


}