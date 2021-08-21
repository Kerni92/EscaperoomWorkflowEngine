package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkflowPartFinishedModel extends AbstractEscaperoomLoadableDetachableModel<Boolean> {

    @SpringBean
    private transient GameExecutionService gameExecutionService;

    private Long gameId = null;

    public WorkflowPartFinishedModel() {
        super();
        Injector.get().inject(this);
    }

    public WorkflowPartFinishedModel(Long gameId) {
        super();
        Injector.get().inject(this);
        this.gameId = gameId;
    }

    @Override
    protected Boolean loadInternal() {
        if (gameId != null) {
            return gameExecutionService.isActiveWorkflowPartInstanceFinished(gameId);
        }
        return Boolean.FALSE;
    }
}
