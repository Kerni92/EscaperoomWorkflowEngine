package de.kernebeck.escaperoom.escaperoomgame.webapp.component;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxSubmitLink;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RiddleComponent extends GenericPanel<RiddleInstance> {

    @SpringBean
    private RiddleInstanceService riddleInstanceService;

    private String solution = "";
    private Boolean isResolved;
    private String labelMessage = "";

    public RiddleComponent(String id, IModel<RiddleInstance> model) {
        super(id, model);

        this.isResolved = getModelObject().isResolved();
        final Riddle riddle = getModelObject().getRiddle();
        final Label riddleDescription = new Label("riddleDescription", riddle.getContent());
        riddleDescription.setEscapeModelStrings(false);
        riddleDescription.setOutputMarkupId(true);
        add(riddleDescription);


        final Label messageLabel = new Label("messageLabel", new PropertyModel<String>(this, "labelMessage"));
        messageLabel.setOutputMarkupId(true);
        add(messageLabel);

        final Form riddleForm = new Form("riddleForm");
        riddleForm.setOutputMarkupId(true);
        final TextField<String> solutionInput = new TextField<>("solutionInput", new PropertyModel<>(this, "solution"));
        solutionInput.setRequired(true);
        solutionInput.setEnabled(!isResolved);
        solutionInput.setOutputMarkupId(true);
        riddleForm.add(solutionInput);
        final AjaxSubmitLink submitLink = new AjaxSubmitLink("checkRiddleButton", JQueryIcon.CIRCLE_CHECK) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                boolean result = riddleInstanceService.checkSolution(getModelObject(), solution);
                if (result) {
                    solutionInput.setEnabled(false);
                    setEnabled(false);
                    labelMessage = "resolved";
                }
                else {
                    labelMessage = "not resolved";
                }
                target.add(riddleForm);
                target.add(messageLabel);
            }


        };
        submitLink.setEnabled(!isResolved);
        submitLink.setOutputMarkupId(true);
        riddleForm.add(submitLink);

        add(riddleForm);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


}
