package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.RiddleSolvedEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class GamePage extends WebPage {

    private static final String CONTENTCOMPONENT_ID = "contentComponent";

    @SpringBean
    private GameService gameService;

    private IModel<Game> gameModel = null;

    private IModel<Boolean> workflowPartFinishedModel = null;

    private IModel<WorkflowPartInstance> workflowPartInstanceModel = null;

    private Label messageLabel;

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

        this.workflowPartFinishedModel = new WorkflowPartFinishedModel(gameId);
        this.workflowPartInstanceModel = new WorkflowPartInstanceModel(gameId);

        add(new WebSocketBehavior() {
            @Override
            protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
                if (message instanceof RiddleSolvedEvent) {
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
        this.messageLabel = new Label("message", Model.of(""));
        this.messageLabel.setOutputMarkupId(true);
        this.messageLabel.setVisible(false);
        add(this.messageLabel);
        this.content = new WebMarkupContainer("content");
        content.setOutputMarkupId(true);
        buildContent(false);
        add(content);
    }


    private void buildContent(boolean isUpdate) {
        if (gameModel != null) {
            final Game game = gameModel.getObject();
            this.messageLabel.setVisible(false);
            Component contentComponent;
            if (game.isFinished()) {
                contentComponent = new GameResultComponent(CONTENTCOMPONENT_ID, gameModel);
            }
            else {
                if (workflowPartFinishedModel.getObject()) {
                    contentComponent = new SelectWorkflowTransitionComponent(CONTENTCOMPONENT_ID, new ValidWorkflowTransitionListModel(workflowPartInstanceModel.getObject().getId())) {

                        @Override
                        public void onSubmit(AjaxRequestTarget target, WorkflowTransition transition) {
                            GamePage.this.submitWorkflowTransitionSelection(target, transition);
                        }

                    };
                }
                else {
                    contentComponent = new WorkflowPartInstanceComponent(CONTENTCOMPONENT_ID, workflowPartInstanceModel, workflowPartFinishedModel);
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
        final boolean success = gameService.executeWorkflowTransition(gameModel.getObject(), transition);
        if (success) {
            gameModel.detach();
            workflowPartFinishedModel.detach();
            workflowPartInstanceModel.detach();
            buildContent(true);
            this.add(content);
            target.add(this);
        }
        else {
            this.messageLabel.setDefaultModelObject("Der nächste Spielausschnitt konnte nicht gestartet werden. Entweder hat ein Mitspieler bereits ein Element ausgewählt oder es ist ein Fehler aufgetreten. Bitte probieren Sie es erneut oder aktualisieren Sie die Seite.");
            this.messageLabel.setVisible(true);
            target.add(this.messageLabel);
        }
    }
}
