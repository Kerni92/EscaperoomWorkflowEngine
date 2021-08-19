package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowTransitionModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import java.util.List;

public abstract class SelectWorkflowTransitionComponent extends GenericPanel<List<WorkflowTransition>> {

    private IModel<String> selectedTransition;

    public SelectWorkflowTransitionComponent(String id, IModel<String> descriptionText, IModel<List<WorkflowTransition>> model) {
        super(id, model);


        add(new Label("workflowPartDescription", descriptionText));
        final Form form = new Form("form");
        final RepeatingView view = new RepeatingView("transitions");
        for (final WorkflowTransition transition : model.getObject()) {
            view.add(new WorkflowTransitionComponent(view.newChildId(), new WorkflowTransitionModel(transition)) {

                @Override
                public void onSubmit(AjaxRequestTarget target, WorkflowTransition workflowTransition) {
                    SelectWorkflowTransitionComponent.this.onSubmit(target, workflowTransition);
                }

            });
        }
        form.add(view);
        add(form);
    }

    public abstract void onSubmit(AjaxRequestTarget target, WorkflowTransition transition);
}
