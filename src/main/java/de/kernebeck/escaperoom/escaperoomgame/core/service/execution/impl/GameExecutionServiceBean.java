package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.ContinueGameEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.PauseGameEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateDialogEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateUIEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.RiddleExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.locking.GameLockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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

    @Autowired
    private EventBus eventBus;

    @Override
    public boolean executeWorkflowTransition(Game game, WorkflowTransition workflowTransition) {
        try {
            gameLockingService.lockGame(game.getId());
            final WorkflowPartInstance workflowPartInstance = workflowExecutionService.executeWorkflowTransition(game, workflowTransition);
            if (workflowPartInstance != null) {
                if (workflowPartInstance.getWorkflowPart().getPartType() == WorkflowPartType.ENDPART) {
                    pauseOrFinishGameInternal(game, Boolean.TRUE);
                }

                return true;
            }
        }
        finally {
            gameLockingService.unlockGame(game.getId());
        }

        return false;
    }

    @Override
    public boolean isActiveWorkflowPartInstanceFinished(Long gameId) {
        final Game game = gameService.load(gameId);
        if (game != null) {
            if (game.getActiveWorkflowPartInstance() != null) {
                return game.getActiveWorkflowPartInstance().getRiddleInstanceList().isEmpty() || game.getActiveWorkflowPartInstance().getRiddleInstanceList().stream().noneMatch(r -> r == null || !r.isResolved());
            }
        }
        return false;
    }

    @Override
    public boolean checkRiddleSolution(Long gameId, RiddleInstance riddleInstance, String solution) {
        try {
            gameLockingService.lockGame(gameId);
            boolean result = riddleExecutionService.checkSolution(riddleInstance, solution);
            if (result) {
                final Game game = gameService.load(gameId);
                if (game != null) {
                    //if this riddle is the last riddle to be solved, we can execute automatically the workflowtransition to go to the next
                    if (isActiveWorkflowPartInstanceFinished(game.getId())) {
                        final WorkflowTransition nextWorkflowTransition = game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions().iterator().next();
                        executeWorkflowTransition(game, nextWorkflowTransition);
                    }

                    //inform ui that riddle is solved and that the content of page has to be rerendered
                    eventBus.post(new UpdateUIEvent(game.getGameId(), riddleInstance.getRiddle().getId()));
                }

            }
            return result;
        }
        finally {
            gameLockingService.unlockGame(gameId);
        }
    }

    @Override
    public RiddleHint getNextRiddleHintForRiddleInstance(Long gameId, RiddleInstance riddleInstance) {
        try {
            gameLockingService.lockGame(gameId);
            final Game game = gameService.load(gameId);
            final RiddleHint hint = riddleExecutionService.getNextRiddleHint(riddleInstance);
            if (hint != null) {
                eventBus.post(new UpdateDialogEvent(game.getGameId()));
            }
            return hint;
        }
        finally {
            gameLockingService.unlockGame(gameId);
        }
    }

    @Override
    public void startGame(Game game) {
        if (game != null) {
            try {
                gameLockingService.lockGame(game.getId());
                final Timestamp time = new Timestamp(System.currentTimeMillis());
                game.setStarttime(time);
                game.setLastStartTime(time);
                game.getActiveWorkflowPartInstance().setStartTime(time);
                game.getActiveWorkflowPartInstance().setLastStartTime(time);
                gameService.save(game);
            }
            finally {
                gameLockingService.unlockGame(game.getId());
            }
        }
    }

    @Override
    public void pauseGame(Game game) {
        if (game != null) {
            try {
                gameLockingService.lockGame(game.getId());
                pauseOrFinishGameInternal(game, Boolean.FALSE);
                eventBus.post(new PauseGameEvent(game.getGameId()));
            }
            finally {
                gameLockingService.unlockGame(game.getId());
            }
        }
    }

    @Override
    public void continueGame(Game game) {
        if (game != null) {
            try {
                gameLockingService.lockGame(game.getId());
                final Timestamp time = new Timestamp(System.currentTimeMillis());
                game.setLastStartTime(time);
                gameService.save(game);

                game.getActiveWorkflowPartInstance().setLastStartTime(time);
                workflowPartInstanceService.save(game.getActiveWorkflowPartInstance());
                eventBus.post(new ContinueGameEvent(game.getGameId()));
            }
            finally {
                gameLockingService.unlockGame(game.getId());
            }
        }
    }

    @Override
    public void finishGame(Game game) {
        if (game != null) {
            try {
                gameLockingService.lockGame(game.getId());
                pauseOrFinishGameInternal(game, Boolean.TRUE);
            }
            finally {
                gameLockingService.unlockGame(game.getId());
            }
        }
    }

    @Override
    public void stopRunningGames() {
        final List<Game> runningGames = gameService.findRunningGames();
        for (final Game g : runningGames) {
            pauseOrFinishGameInternal(g, false);
        }
    }

    private void pauseOrFinishGameInternal(Game game, Boolean finished) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());

        //save game values
        final long elapsedGameTime = time.getTime() - game.getLastStartTime().getTime();
        game.setTotalTime(game.getTotalTime() == null ? elapsedGameTime : game.getTotalTime() + elapsedGameTime);
        game.setLastStartTime(null);
        game.setEndTime(time);
        game.setFinished(finished);
        gameService.save(game);

        final WorkflowPartInstance activeWorkflowPartInstance = game.getActiveWorkflowPartInstance();
        final long elapsedWorkflowPartTime = time.getTime() - activeWorkflowPartInstance.getLastStartTime().getTime();
        activeWorkflowPartInstance.setTotalTime(activeWorkflowPartInstance.getTotalTime() == null ? elapsedGameTime : activeWorkflowPartInstance.getTotalTime() + elapsedWorkflowPartTime);
        activeWorkflowPartInstance.setEndTime(time);
        activeWorkflowPartInstance.setLastStartTime(null);
        workflowPartInstanceService.save(activeWorkflowPartInstance);
    }


}
