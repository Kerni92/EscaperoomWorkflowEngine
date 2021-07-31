package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartFinishedModel extends LoadableDetachableModel<Boolean> {

    @SpringBean
    private WorkflowPartInstanceService workflowPartInstanceService;

    @SpringBean
    private GameService gameService;

    private String gameId = null;

    public WorkflowPartFinishedModel() {
        super();
        Injector.get().inject(this);
    }

    public WorkflowPartFinishedModel(String gameId) {
        super();
        Injector.get().inject(this);
        this.gameId = gameId;
    }

    @Override
    protected Boolean load() {
        if (gameId != null) {
            final Game game = gameService.findByGameId(gameId);
            if (game.getActiveWorkflowPartInstance() != null) {
                return game.getActiveWorkflowPartInstance().getRiddleInstanceList().isEmpty() || game.getActiveWorkflowPartInstance().getRiddleInstanceList().stream().noneMatch(r -> r == null || !r.isResolved());
            }
        }
        return Boolean.FALSE;
    }
}
