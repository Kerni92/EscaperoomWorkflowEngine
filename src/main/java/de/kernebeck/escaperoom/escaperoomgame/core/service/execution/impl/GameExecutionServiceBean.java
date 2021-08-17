package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.RiddleExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.locking.GameLockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GameExecutionServiceBean implements GameExecutionService {

    @Autowired
    private GameService gameService;

    @Autowired
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Autowired
    private GameLockingService gameLockingService;

    @Autowired
    private WorkflowExecutionService workflowExecutionService;

    @Autowired
    private RiddleExecutionService riddleExecutionService;

    @Override
    public boolean executeWorkflowTransition(Game game, WorkflowTransition workflowTransition) {
        try {
            gameLockingService.lockGame(game.getId());
            final WorkflowPartInstance workflowPartInstance = workflowExecutionService.executeWorkflowTransition(game, workflowTransition);
            if (workflowPartInstance != null) {
                return true;
            }
        }
        finally {
            gameLockingService.unlockGame(game.getId());
        }

        return false;
    }

    @Override
    public boolean checkRiddleSolution(Long gameId, RiddleInstance riddleInstance, String solution) {
        try {
            gameLockingService.lockGame(gameId);
            return riddleExecutionService.checkSolution(riddleInstance, solution);
        }
        finally {
            gameLockingService.unlockGame(gameId);
        }
    }

    @Override
    public void startGame(Game game) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());
        game.setStarttime(time);
        game.setLastStartTime(time);
        game.getActiveWorkflowPartInstance().setStartTime(time);
        game.getActiveWorkflowPartInstance().setLastStartTime(time);
        gameService.save(game);
    }

    @Override
    public void pauseGame(Game game) {
        pauseOrFinishGameInternal(game, Boolean.FALSE);
    }

    @Override
    public void continueGame(Game game) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());
        game.setLastStartTime(time);
        gameService.save(game);

        game.getActiveWorkflowPartInstance().setLastStartTime(time);
        workflowPartInstanceService.save(game.getActiveWorkflowPartInstance());
    }

    @Override
    public void finishGame(Game game) {
        pauseOrFinishGameInternal(game, Boolean.TRUE);
    }

    private void pauseOrFinishGameInternal(Game game, Boolean finished) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());

        //save game values
        final long elapsedGameTime = time.getTime() - game.getLastStartTime().getTime();
        game.setTotalTime(game.getTotalTime() == null ? elapsedGameTime : game.getTotalTime() + elapsedGameTime);
        game.setFinished(finished);
        gameService.save(game);

        final WorkflowPartInstance activeWorkflowPartInstance = game.getActiveWorkflowPartInstance();
        final long elapsedWorkflowPartTime = time.getTime() - activeWorkflowPartInstance.getLastStartTime().getTime();
        activeWorkflowPartInstance.setTotalTime(activeWorkflowPartInstance.getTotalTime() == null ? elapsedGameTime : activeWorkflowPartInstance.getTotalTime() + elapsedWorkflowPartTime);
        activeWorkflowPartInstance.setEndTime(time);
        workflowPartInstanceService.save(activeWorkflowPartInstance);
    }


}
