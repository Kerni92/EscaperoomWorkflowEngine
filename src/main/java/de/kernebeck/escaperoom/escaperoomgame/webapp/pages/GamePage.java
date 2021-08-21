package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateUIEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.gameresult.GameResultComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance.WorkflowPartInstanceComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition.SelectWorkflowTransitionComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.GameModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.ValidWorkflowTransitionListModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowPartFinishedModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowPartInstanceModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.service.WebSocketEventService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.util.Collections;
import java.util.List;

public class GamePage extends WebPage {

    private static final String CONTENTCOMPONENT_ID = "contentComponent";

    @SpringBean
    private GameExecutionService gameExecutionService;

    private IModel<Game> gameModel = null;

    private IModel<Boolean> workflowPartFinishedModel = null;

    private IModel<WorkflowPartInstance> workflowPartInstanceModel = null;

    private Label messageLabel;

    private WebMarkupContainer dialog;
    private WebMarkupContainer content;

    public GamePage() {
        super();
    }

    public GamePage(PageParameters pageParameters) {
        super(pageParameters);
        final String gameId = pageParameters.get("gameId") != null ? pageParameters.get("gameId").toString() : null;
        if (gameId != null) {
            gameModel = new GameModel(gameId);
        }

        final Long technicalGameId = gameModel != null ? gameModel.getObject() != null ? gameModel.getObject().getId() : null : null;
        this.workflowPartFinishedModel = new WorkflowPartFinishedModel(technicalGameId);
        this.workflowPartInstanceModel = new WorkflowPartInstanceModel(gameId);

        add(new WebSocketBehavior() {
            @Override
            protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
                if (message instanceof UpdateUIEvent) {
                    workflowPartFinishedModel.detach();
                    workflowPartInstanceModel.detach();
                    buildContent(true);
                    handler.add(content);
                }
            }

            @Override
            protected void onConnect(ConnectedMessage message) {
                WebSocketEventService.getInstance().registerConnection(message, gameId);
            }

            @Override
            protected void onClose(ClosedMessage message) {
                WebSocketEventService.getInstance().unregisterConnection(message, gameId);
            }
        });
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        //build dialog container
        initDialog();

        //build content container
        this.content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);

        //add header to content container
        final String gameName = gameModel != null && gameModel.getObject() != null ? gameModel.getObject().getWorkflow().getName() : "Kein Spiel ausgewählt";
        final String gameId = gameModel != null && gameModel.getObject() != null ? gameModel.getObject().getGameId() : "";
        final List<String> playerNames = gameModel != null && gameModel.getObject() != null ? gameModel.getObject().getUsernames() : Collections.emptyList();

        final Label workflowName = new Label("workflowName", Model.of(gameName));
        final Label gameIdLabel = new Label("gameId", Model.of(gameId));
        content.add(workflowName);
        content.add(gameIdLabel);
        final RepeatingView playerNamesList = new RepeatingView("playerNames");
        for (final String player : playerNames) {
            playerNamesList.add(new Label(playerNamesList.newChildId(), Model.of(player)));
        }
        content.add(playerNamesList);


        //add messageLabel
        this.messageLabel = new Label("message", Model.of(""));
        this.messageLabel.setOutputMarkupId(true);
        this.messageLabel.setVisible(false);
        add(this.messageLabel);

        buildContent(false);
        add(content);
    }


    private void buildContent(boolean isUpdate) {
        if (gameModel != null) {
            final Game game = gameModel.getObject();
            this.messageLabel.setVisible(false);
            Component contentComponent;
            if (game.isFinished() || workflowPartInstanceModel.getObject().getWorkflowPart().getPartType() == WorkflowPartType.ENDPART) {
                contentComponent = new GameResultComponent(CONTENTCOMPONENT_ID, gameModel);
            }
            else {
                if (workflowPartInstanceModel.getObject().getWorkflowPart().getPartType() == WorkflowPartType.DECISION) {
                    final ValidWorkflowTransitionListModel model = new ValidWorkflowTransitionListModel(workflowPartInstanceModel.getObject().getId());
                    contentComponent = new SelectWorkflowTransitionComponent(CONTENTCOMPONENT_ID, Model.of(workflowPartInstanceModel.getObject().getWorkflowPart().getDescription()), model) {

                        @Override
                        public void onSubmit(AjaxRequestTarget target, WorkflowTransition transition) {
                            GamePage.this.submitWorkflowTransitionSelection(target, transition);
                        }

                    };
                }
                else {
                    contentComponent = new WorkflowPartInstanceComponent(CONTENTCOMPONENT_ID, workflowPartInstanceModel, workflowPartFinishedModel,
                            (SerializableBiFunction<AjaxRequestTarget, WebMarkupContainer, String>) (target, components) -> {
                                GamePage.this.showDialog(components, target);
                                return null;
                            },
                            (SerializableFunction<AjaxRequestTarget, String>) target -> {
                                GamePage.this.hideDialog(target);
                                return null;
                            }
                    );
                }
            }
            contentComponent.setOutputMarkupId(true);
            if (isUpdate) {
                content.replace(contentComponent);
            }
            else {
                content.add(contentComponent);
            }
        }
        else {
            this.messageLabel.setDefaultModelObject("Es wurde keine gültige Spielid übergeben.");
            this.messageLabel.setVisible(true);
            content.add(new EmptyPanel(CONTENTCOMPONENT_ID));
        }
    }

    private void submitWorkflowTransitionSelection(AjaxRequestTarget target, WorkflowTransition transition) {
        gameModel.detach(); //reload game always if to assure that the active workflowpartinstance is corretly loaded
        final boolean success = gameExecutionService.executeWorkflowTransition(gameModel.getObject(), transition);
        if (success) {
            gameModel.detach();
            workflowPartFinishedModel.detach();
            workflowPartInstanceModel.detach();
            buildContent(true);
            this.add(content);
            if (target != null) {
                target.add(this);
            }
        }
        else {
            this.messageLabel.setDefaultModelObject("Der nächste Spielausschnitt konnte nicht gestartet werden. Entweder hat ein Mitspieler bereits ein Element ausgewählt oder es ist ein Fehler aufgetreten. Bitte probieren Sie es erneut oder aktualisieren Sie die Seite.");
            this.messageLabel.setVisible(true);
            if (target != null) {
                target.add(this.messageLabel);
            }
        }
    }

    private void initDialog() {
        dialog = new WebMarkupContainer("dialog");
        dialog.setVisible(false);
        dialog.setOutputMarkupId(true);
        dialog.setOutputMarkupPlaceholderTag(true);
        final WebMarkupContainer dialogContent = new WebMarkupContainer("dialogContent");
        dialogContent.setOutputMarkupId(true);
        dialog.add(dialogContent);
        add(dialog);
    }

    private void showDialog(WebMarkupContainer dialogContent, AjaxRequestTarget target) {
        dialogContent.setOutputMarkupId(true);
        dialog.replace(dialogContent);
        dialog.setVisible(true);
        target.add(dialog);
    }

    private void hideDialog(AjaxRequestTarget target) {
        dialog.replace(new WebMarkupContainer("dialogContent"));
        dialog.setVisible(false);
        target.add(dialog);
    }
}
