package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.gameresult.GameResultComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance.WorkflowPartInstanceComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowtransition.SelectWorkflowTransitionComponent;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.GameModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.ValidWorkflowTransitionListModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowPartFinishedModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowPartInstanceModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class GamePage extends WebPage {

    private static final String CONTENTCOMPONENT_ID = "contentComponent";

    @SpringBean
    private GameService gameService;

    private IModel<Game> gameModel = null;

    private IModel<Boolean> workflowPartFinishedModel = null;

    private IModel<WorkflowPartInstance> workflowPartInstanceModel = null;

    public GamePage() {
        super();
        add(new Label("test", "Es wurde kein Spiel übergeben"));
    }

    public GamePage(PageParameters pageParameters) {
        super(pageParameters);
        final String gameId = pageParameters.get("gameId") != null ? pageParameters.get("gameId").toString() : null;
        if (gameId != null) {
            gameModel = new GameModel(gameId);
        }

        this.workflowPartFinishedModel = new WorkflowPartFinishedModel(gameModel.getObject().getActiveWorkflowPartInstance().getId());
        this.workflowPartInstanceModel = new WorkflowPartInstanceModel(gameModel.getObject().getActiveWorkflowPartInstance().getId());
        //final AjaxSubmitLink finishWorkflowPartButton = new AjaxSubmitLink("finishWorkflowPartButton") {


        //   @Override
        // protected void onSubmit(AjaxRequestTarget target) {
        //   super.onSubmit(target);
        //todo trigger event for page to update and reload
        //}

        //@Override
        // public boolean isEnabled() {
        //  return workflowPartFinishedModel.getObject();
        //}
        //};
        //add(finishWorkflowPartButton);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(buildContentContainer());
    }


    private WebMarkupContainer buildContentContainer() {
        final WebMarkupContainer content = new WebMarkupContainer("content");
        final Game game = gameModel.getObject();
        if (game.isFinished()) {
            content.add(new GameResultComponent(CONTENTCOMPONENT_ID, gameModel));
        }
        else {
            if (workflowPartFinishedModel.getObject()) {
                content.add(new SelectWorkflowTransitionComponent(CONTENTCOMPONENT_ID, new ValidWorkflowTransitionListModel(workflowPartInstanceModel.getObject().getId())));
            }
            else {
                content.add(new WorkflowPartInstanceComponent(CONTENTCOMPONENT_ID, workflowPartInstanceModel, workflowPartFinishedModel));
            }
        }

        return content;
    }
}
