package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.RiddleInstanceModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableFunction;

public class WorkflowPartInstanceComponent extends GenericPanel<WorkflowPartInstance> {

    private IModel<Boolean> workflowPartFinishedModel;

    public WorkflowPartInstanceComponent(String id, IModel<WorkflowPartInstance> model, IModel<Boolean> workflowPartFinishedModel, SerializableBiFunction<AjaxRequestTarget, WebMarkupContainer, String> showDialogFunction, SerializableFunction<AjaxRequestTarget, String> closeDialogFunction) {
        super(id, model);
        this.workflowPartFinishedModel = workflowPartFinishedModel;
        final WorkflowPartInstance workflowPartInstance = getModelObject();
        add(new Label("workflowPartDescription", Model.of(workflowPartInstance.getWorkflowPart().getDescription())));
        final RepeatingView riddles = new RepeatingView("riddles");
        if (workflowPartInstance.getRiddleInstanceList() != null && !workflowPartInstance.getRiddleInstanceList().isEmpty()) {
            for (RiddleInstance ri : workflowPartInstance.getRiddleInstanceList()) {
                riddles.add(new RiddleComponent(riddles.newChildId(), Model.of(workflowPartInstance.getGame().getId()), new RiddleInstanceModel(ri.getId()), showDialogFunction, closeDialogFunction));
            }
        }
        add(riddles);
    }


}
