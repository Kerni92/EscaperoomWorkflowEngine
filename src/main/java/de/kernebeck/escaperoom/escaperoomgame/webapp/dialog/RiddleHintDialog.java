package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance.RiddleHintComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.RiddleHintModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Comparator;
import java.util.List;

public abstract class RiddleHintDialog extends AbstractDialog<RiddleInstance> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private transient GameExecutionService gameExecutionService;

    @SpringBean
    private transient RiddleHintService riddleHintService;

    private Long gameId;
    private String errorMessage = "";


    private Form<?> form;
    private WebMarkupContainer feedback;
    private WebMarkupContainer riddleHintContainer;
    private AjaxSubmitLink showNextHintButton;

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
        riddleHintContainer = new WebMarkupContainer("riddleHintContainer");

        final List<RiddleHint> usedHintList = riddleHintService.findUsedRiddleHintsForRiddleInstance(getModelObject());
        final List<RiddleHint> allRiddleHints = riddleHintService.findAllRiddleHintByRiddle(getModelObject().getRiddle());

        final RepeatingView riddleHints = buildRiddleHintsView(usedHintList);
        riddleHintContainer.add(riddleHints);
        riddleHintContainer.setOutputMarkupId(true);
        riddleHintContainer.setOutputMarkupPlaceholderTag(true);
        form.add(riddleHintContainer);

        showNextHintButton = new AjaxSubmitLink("showNextHint") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Injector.get().inject(RiddleHintDialog.this);
                final RiddleHint nextHint = gameExecutionService.getNextRiddleHintForRiddleInstance(gameId, getModelObject());
                if (nextHint != null) {
                    final List<RiddleHint> usedHintList = riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstanceModel.getObject());
                    final List<RiddleHint> allRiddleHints = riddleHintService.findAllRiddleHintByRiddle(riddleInstanceModel.getObject().getRiddle());

                    final RepeatingView riddleHints = buildRiddleHintsView(usedHintList);
                    riddleHintContainer.replace(riddleHints);

                    this.setEnabled(usedHintList.size() < allRiddleHints.size());

                    target.add(riddleHintContainer);
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

    @Override
    public void updateDialog(IPartialPageRequestHandler target) {
        Injector.get().inject(this);
        final List<RiddleHint> usedHintList = riddleHintService.findUsedRiddleHintsForRiddleInstance(getModelObject());
        final List<RiddleHint> allRiddleHints = riddleHintService.findAllRiddleHintByRiddle(getModelObject().getRiddle());
        final RepeatingView riddleHints = buildRiddleHintsView(usedHintList);
        riddleHintContainer.replace(riddleHints);
        showNextHintButton.setEnabled(usedHintList.size() < allRiddleHints.size());

        target.add(riddleHintContainer);
        target.add(showNextHintButton);
    }

    private RepeatingView buildRiddleHintsView(final List<RiddleHint> usedHintList) {
        final RepeatingView riddleHints = new RepeatingView("riddleHints");
        riddleHints.setOutputMarkupId(true);
        riddleHints.setOutputMarkupPlaceholderTag(true);

        usedHintList.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        for (final RiddleHint h : usedHintList) {
            riddleHints.add(new RiddleHintComponent(riddleHints.newChildId(), new RiddleHintModel(h)));
        }

        return riddleHints;
    }

    private void handleError(AjaxRequestTarget target, String errorMessage) {
        this.errorMessage = errorMessage;
        feedback.setVisible(true);
        target.add(feedback);
    }

}
