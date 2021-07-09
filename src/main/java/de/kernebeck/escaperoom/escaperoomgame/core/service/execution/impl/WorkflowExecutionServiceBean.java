package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowExecutionServiceBean implements WorkflowExecutionService {

    private static final Map<Long, String> BLOCKEDITEMS = new ConcurrentHashMap<>();

    @Autowired
    private GameService gameService;

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;


    @Override
    public boolean isTransitionExecutionPossible(Game game, WorkflowTransition workflowTransition) {
        if (game != null && workflowTransition != null && game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions().stream().map(WorkflowTransition::getId).anyMatch(id -> id.equals(workflowTransition.getId()))) {
            return true;
        }
        return false;
    }

    @Override
    public WorkflowPartInstance executeWorkflowTransition(Game game, WorkflowTransition workflowTransition) {
        //TODO implement locking
        if (isTransitionExecutionPossible(game, workflowTransition)) {
            //first finish current workflowinstance
            final WorkflowPartInstance activeWorkflowPart = game.getActiveWorkflowPartInstance();
            final long currentTime = System.currentTimeMillis();
            final long startTime = activeWorkflowPart.getLastStartTime().getTime();

            activeWorkflowPart.setEndTime(new Timestamp(currentTime));
            activeWorkflowPart.setTotalTime((activeWorkflowPart.getTotalTime() != null ? activeWorkflowPart.getTotalTime() : 0) + (startTime - currentTime));

            workflowPartInstanceRepository.save(activeWorkflowPart);

            //second create new one for next workflowpart
            final Timestamp starttime = new Timestamp(System.currentTimeMillis());
            final WorkflowPartInstance nextActive = new WorkflowPartInstance(workflowTransition.getDestinationPart(), null, starttime, starttime, null, game);
            workflowPartInstanceRepository.save(nextActive);

            //create riddle instance
            for (final Riddle r : nextActive.getWorkflowPart().getRiddles()) {
                final RiddleInstance rI = new RiddleInstance(r, Collections.emptyList(), activeWorkflowPart, 0, false);
                riddleInstanceRepository.save(rI);
            }

            game.setCurrentWorkflowpart(nextActive);
            gameService.save(game);

            return workflowPartInstanceRepository.findById(nextActive.getId()).get(); //should never be null
        }

        return null;
    }


}
