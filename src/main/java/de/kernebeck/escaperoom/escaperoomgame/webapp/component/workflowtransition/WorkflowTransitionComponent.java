package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public abstract class WorkflowTransitionComponent extends GenericPanel<WorkflowTransition> {

    public WorkflowTransitionComponent(String id, IModel<WorkflowTransition> model) {
        super(id, model);

        final Label description = new Label("description", model.getObject().getDescription());
        final AjaxButton submitLink = new AjaxButton("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                WorkflowTransitionComponent.this.onSubmit(target, WorkflowTransitionComponent.this.getModelObject());
            }
        };

        add(description);
        add(submitLink);
    }

    public abstract void onSubmit(AjaxRequestTarget target, WorkflowTransition workflowTransition);

}
