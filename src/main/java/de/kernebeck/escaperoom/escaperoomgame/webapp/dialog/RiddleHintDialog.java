package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance.RiddleHintComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.RiddleHintModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Comparator;
import java.util.List;

public abstract class RiddleHintDialog extends GenericPanel<RiddleInstance> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private GameService gameService;

    @SpringBean
    private GameExecutionService gameExecutionService;

    @SpringBean
    private RiddleHintService riddleHintService;

    private Long gameId;
    private String errorMessage = "";


    private Form<?> form;
    private WebMarkupContainer feedback;
    private RepeatingView riddleHints;

    public RiddleHintDialog(String id, Long gameId, IModel<RiddleInstance> riddleInstanceModel) {
        super(id, riddleInstanceModel);

        this.gameId = gameId;

        this.form = new Form<>("dialogForm");

        //add feedback panel
        this.feedback = new WebMarkupContainer("feedbackPanel");
        feedback.add(new Label("messageLabel", new PropertyModel<>(this, "errorMessage")));
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        feedback.setVisible(false);
        form.add(feedback);

        //display riddle hints
        riddleHints = new RepeatingView("riddleHints");

        final List<RiddleHint> usedHintList = riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstanceModel.getObject());
        final List<RiddleHint> allRiddleHints = riddleHintService.findAllRiddleHintByRiddle(riddleInstanceModel.getObject().getRiddle());
        usedHintList.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        for (final RiddleHint h : usedHintList) {
            riddleHints.add(new RiddleHintComponent(riddleHints.newChildId(), new RiddleHintModel(h)));
        }
        riddleHints.setOutputMarkupId(true);
        riddleHints.setOutputMarkupPlaceholderTag(true);
        form.add(riddleHints);

        final AjaxSubmitLink showNextHintButton = new AjaxSubmitLink("showNextHint") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                final RiddleHint nextHint = gameExecutionService.getNextRiddleHintForRiddleInstance(gameId, getModelObject());
                if (nextHint != null) {
                    riddleHints.add(new RiddleHintComponent(riddleHints.newChildId(), new RiddleHintModel(nextHint)));

                    final List<RiddleHint> usedHintList = riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstanceModel.getObject());
                    final List<RiddleHint> allRiddleHints = riddleHintService.findAllRiddleHintByRiddle(riddleInstanceModel.getObject().getRiddle());
                    this.setEnabled(usedHintList.size() < allRiddleHints.size());

                    target.add(form);
                }
                else {
                    this.setEnabled(false);
                }
                target.add(this);
            }
        };
        showNextHintButton.setEnabled(!riddleInstanceModel.getObject().isResolved() && usedHintList.size() < allRiddleHints.size());
        form.add(showNextHintButton);

        form.add(new AjaxLink<Void>("closeDialog") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                RiddleHintDialog.this.closeDialog(target);
            }

        });
        form.add(new AjaxLink<Void>("closeDialogX") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                RiddleHintDialog.this.closeDialog(target);
            }

        });
        this.add(form);
    }

    public abstract void closeDialog(AjaxRequestTarget target);

    private void handleError(AjaxRequestTarget target, String errorMessage) {
        this.errorMessage = errorMessage;
        feedback.setVisible(true);
        target.add(feedback);
    }

}
