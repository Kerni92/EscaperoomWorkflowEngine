package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartInstanceModel extends AbstractEscaperoomLoadableDetachableModel<WorkflowPartInstance> {

    @SpringBean
    private transient GameService gameService;

    private String gameId;

    public WorkflowPartInstanceModel() {
        //required for serialization and deserialization
        super();
        Injector.get().inject(this);
    }

    public WorkflowPartInstanceModel(String gameId) {
        super();
        Injector.get().inject(this);
        this.gameId = gameId;
    }

    @Override
    protected WorkflowPartInstance loadInternal() {
        if (gameId != null) {
            final Game game = gameService.findByGameId(gameId);
            if (game != null) {
                return game.getActiveWorkflowPartInstance();
            }
        }
        return null;
    }
}
