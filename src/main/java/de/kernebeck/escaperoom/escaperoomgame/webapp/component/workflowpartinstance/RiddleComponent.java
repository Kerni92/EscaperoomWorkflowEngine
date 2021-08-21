package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.dialog.RiddleHintDialog;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableFunction;

public class RiddleComponent extends GenericPanel<RiddleInstance> {

    @SpringBean
    private transient GameExecutionService gameExecutionService;

    private String solution = "";
    private Boolean isResolved;
    private String labelMessage;

    private IModel<Long> gameIdModel;

    private SerializableBiFunction<AjaxRequestTarget, WebMarkupContainer, String> showDialogFunction;
    private SerializableFunction<AjaxRequestTarget, String> closeDialogFunction;

    public RiddleComponent(String id, IModel<Long> gameIdModel, IModel<RiddleInstance> model, SerializableBiFunction<AjaxRequestTarget, WebMarkupContainer, String> showDialogFunction, SerializableFunction<AjaxRequestTarget, String> closeDialogFunction) {
        super(id, model);

        this.showDialogFunction = showDialogFunction;
        this.closeDialogFunction = closeDialogFunction;
        this.gameIdModel = gameIdModel;
        this.isResolved = getModelObject().isResolved();
        final Riddle riddle = getModelObject().getRiddle();
        final Label riddleDescription = new Label("riddleDescription", riddle.getContent());
        riddleDescription.setEscapeModelStrings(false);
        riddleDescription.setOutputMarkupId(true);
        add(riddleDescription);


        this.labelMessage = isResolved ? "Rätsel gelöst" : "Noch kein Lösungsversuch gestartet";
        final Label messageLabel = new Label("messageLabel", new PropertyModel<String>(this, "labelMessage"));
        add(messageLabel);
        messageLabel.setOutputMarkupId(true);

        final Form riddleForm = new Form("riddleForm");
        riddleForm.setOutputMarkupId(true);
        final TextField<String> solutionInput = new TextField<>("solutionInput", new PropertyModel<>(this, "solution"));
        solutionInput.setRequired(true);
        solutionInput.setEnabled(!isResolved);
        solutionInput.setOutputMarkupId(true);
        riddleForm.add(solutionInput);
        final AjaxButton submitLink = new AjaxButton("checkRiddleButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                Injector.get().inject(RiddleComponent.this);
                boolean result = gameExecutionService.checkRiddleSolution(RiddleComponent.this.gameIdModel.getObject(), RiddleComponent.this.getModelObject(), solution);
                if (result) {
                    solutionInput.setEnabled(false);
                    setEnabled(false);
                    labelMessage = "Rätsel gelöst";
                }
                else {
                    labelMessage = "Die eingegebene Lösung ist leider falsch.";
                }
                target.add(riddleForm);
                target.add(messageLabel);
            }
        };
        submitLink.setEnabled(!isResolved);
        submitLink.setOutputMarkupId(true);
        riddleForm.add(submitLink);

        final AjaxLink<String> showHintDialogButton = new AjaxLink<String>("showHintDialog") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                showDialogFunction.apply(target, new RiddleHintDialog("dialogContent", gameIdModel.getObject(), model) {
                    @Override
                    public void closeDialog(AjaxRequestTarget target) {
                        closeDialogFunction.apply(target);
                    }
                });
            }
        };
        showHintDialogButton.setOutputMarkupId(true);
        riddleForm.add(showHintDialogButton);

        add(riddleForm);
    }
}
