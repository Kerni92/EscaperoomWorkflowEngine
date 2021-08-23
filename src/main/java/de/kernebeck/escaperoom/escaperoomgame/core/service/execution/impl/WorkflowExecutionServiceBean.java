package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateUIEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowExecutionServiceBean implements WorkflowExecutionService {

    private static final Map<Long, String> BLOCKEDITEMS = new ConcurrentHashMap<>();

    @Autowired
    private GameService gameService;

    @Autowired
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Autowired
    private EventBus eventBus;

    @Override
    public boolean isTransitionExecutionPossible(Game game, WorkflowTransition workflowTransition) {
        if (game != null && workflowTransition != null && game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions().stream().map(WorkflowTransition::getId).anyMatch(id -> id.equals(workflowTransition.getId()))) {
            return true;
        }
        return false;
    }

    @Override
    public WorkflowPartInstance executeWorkflowTransition(Game game, WorkflowTransition workflowTransition) {

        if (isTransitionExecutionPossible(game, workflowTransition)) {
            //first finish current workflowinstance
            final WorkflowPartInstance activeWorkflowPart = game.getActiveWorkflowPartInstance();
            final long currentTime = System.currentTimeMillis();
            final long startTime = activeWorkflowPart.getLastStartTime().getTime();

            activeWorkflowPart.setEndTime(new Timestamp(currentTime));
            activeWorkflowPart.setTotalTime((activeWorkflowPart.getTotalTime() != null ? activeWorkflowPart.getTotalTime() : 0) + (currentTime - startTime));

            workflowPartInstanceService.save(activeWorkflowPart); //sinnloser kommentar

            //second create new one for next workflowpart
            WorkflowPartInstance nextActive = workflowPartInstanceService.createWorkflowPartInstanceFromWorkflowPart(game, workflowTransition.getDestinationPart(), new Timestamp(System.currentTimeMillis()));

            game.setCurrentWorkflowpart(nextActive);
            gameService.save(game);

            //if next workflowpart is of type part and has no riddles active, we should execute directly the first next one, because no interaction is required to do anything here
            if (nextActive.getWorkflowPart().getPartType() == WorkflowPartType.PART && nextActive.getRiddleInstanceList().isEmpty()) {
                final List<WorkflowTransition> outgoingTransitions = new ArrayList<>(nextActive.getWorkflowPart().getOutgoingTransitions());
                outgoingTransitions.sort(Comparator.comparingInt(WorkflowTransition::getSortIndex));

                nextActive = executeWorkflowTransition(game, outgoingTransitions.get(0));
                if (nextActive == null) {
                    return null;
                }
            }

            eventBus.post(new UpdateUIEvent(game.getGameId(), null));
            return workflowPartInstanceService.findWorkflowPartInstanceById(nextActive.getId()); //should never be null
        }

        return null;
    }


}
