package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class WorkflowTransitionComponent extends GenericPanel<WorkflowTransition> {

    public WorkflowTransitionComponent(String id, IModel<WorkflowTransition> model) {
        super(id, model);

        final Label description = new Label("description", model.getObject().getDescription());
        final AjaxSubmitLink submitLink = new AjaxSubmitLink("submitLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                WorkflowTransitionComponent.this.onSubmit(target, getModelObject());
            }
        };
        submitLink.setBody(Model.of(model.getObject().getName()));

        add(description);
        add(submitLink);
    }

    public abstract void onSubmit(AjaxRequestTarget target, WorkflowTransition workflowTransition);

}
