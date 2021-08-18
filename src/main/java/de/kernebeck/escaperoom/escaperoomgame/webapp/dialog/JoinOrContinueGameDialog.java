package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.GamePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class JoinOrContinueGameDialog extends GenericPanel<String> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private GameService gameService;

    @SpringBean
    private GameExecutionService gameExecutionService;

    private String gameId;
    private String errorMessage = "";


    private Form<?> form;
    private WebMarkupContainer feedback;

    public JoinOrContinueGameDialog(String id, IModel<String> title) {
        super(id, title);


        this.form = new Form<>("dialogForm");

        this.feedback = new WebMarkupContainer("feedbackPanel");
        feedback.add(new Label("messageLabel", new PropertyModel<>(this, "errorMessage")));
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        feedback.setVisible(false);
        form.add(feedback);

        form.add(new Label("description", new ResourceModel("dialog.joinorcontinue.description", "dialog.joinorcontinue.description")));

        form.add(new TextField<String>("gameId", new PropertyModel<>(this, "gameId")));

        form.add(new AjaxSubmitLink("continueOrJoinGame") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (gameId == null) {
                    handleError(target, "Es wurde kein Spiel ID eingegeben.");
                    return;
                }
                final Game game = gameService.findByGameId(gameId);
                if (game == null) {
                    handleError(target, "Das angegebene Spiel konnte nicht in der Datenbank gefunden werden. Bitte prüfen Sie ihre Eingabe");
                    return;
                }
                if (game.isFinished()) {
                    handleError(target, "Das angegebene Spiel ist bereits abgeschlossen. Daher kann dem Spiel nicht beigetreten oder es Fortgesetzt werden.");
                    return;
                }

                if (game.getLastStartTime() == null) { //if game is stoppend -> continue
                    gameExecutionService.continueGame(game);
                }

                final PageParameters pageParameters = new PageParameters();
                pageParameters.add("gameId", game.getGameId());
                closeDialog(target);
                setResponsePage(GamePage.class, pageParameters);

            }
        });

        form.add(new AjaxLink<Void>("closeDialog") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                JoinOrContinueGameDialog.this.closeDialog(target);
            }

        });
        form.add(new AjaxLink<Void>("closeDialogX") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                JoinOrContinueGameDialog.this.closeDialog(target);
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
