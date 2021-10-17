package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.ContinueGameEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.PauseGameEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateDialogEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.RiddleExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.locking.GameLockingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameExecutionServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private GameLockingService gameLockingService;

    @Mock
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Mock
    private WorkflowExecutionService workflowExecutionService;

    @Mock
    private RiddleExecutionService riddleExecutionService;

    @Mock
    private EventBus eventBus;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameExecutionServiceBean gameExecutionServiceBean;


    @Test
    public void testExecuteWorkflowTransition_Success() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart part = mock(WorkflowPart.class);
        when(part.getPartType()).thenReturn(WorkflowPartType.PART);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(part);
        when(workflowExecutionService.executeWorkflowTransition(game, workflowTransition)).thenReturn(workflowPartInstance);

        assertThat(gameExecutionServiceBean.executeWorkflowTransition(game, workflowTransition)).isTrue();
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowExecutionService).executeWorkflowTransition(game, workflowTransition);
    }

    @Test
    public void testExecuteWorkflowTransition_EndGame() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 1000000));
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart part = mock(WorkflowPart.class);
        when(part.getPartType()).thenReturn(WorkflowPartType.ENDPART);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(part);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 2000000));
        when(workflowExecutionService.executeWorkflowTransition(game, workflowTransition)).thenReturn(workflowPartInstance);

        assertThat(gameExecutionServiceBean.executeWorkflowTransition(game, workflowTransition)).isTrue();
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowExecutionService).executeWorkflowTransition(game, workflowTransition);
        verify(workflowPartInstance).setLastStartTime(null);
        verify(workflowPartInstance).setTotalTime(any(Long.class));
        verify(workflowPartInstance).setEndTime(any(Timestamp.class));
        verify(game).setFinished(true);
        verify(game).setLastStartTime(null);
        verify(game).setTotalTime(any(Long.class));
        verify(game).setEndTime(any(Timestamp.class));
        verify(gameService).save(game);
        verify(workflowPartInstanceService).save(workflowPartInstance);
    }

    @Test
    public void testExecuteWorkflowTransition_Failure() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        when(workflowExecutionService.executeWorkflowTransition(game, workflowTransition)).thenReturn(null);

        assertThat(gameExecutionServiceBean.executeWorkflowTransition(game, workflowTransition)).isFalse();
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowExecutionService).executeWorkflowTransition(game, workflowTransition);
    }

    @Test
    public void testIsActiveWorkflowPartInstanceFinished_Finished() {
        final Game game = mock(Game.class);
        when(gameService.load(1L)).thenReturn(game);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance r = mock(RiddleInstance.class);
        when(r.isResolved()).thenReturn(true);
        final RiddleInstance r1 = mock(RiddleInstance.class);
        when(r1.isResolved()).thenReturn(true);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Arrays.asList(r, r1));
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);

        assertThat(gameExecutionServiceBean.isActiveWorkflowPartInstanceFinished(1L)).isTrue();
    }

    @Test
    public void testIsActiveWorkflowPartInstanceFinished_NotFinished() {
        final Game game = mock(Game.class);
        when(gameService.load(1L)).thenReturn(game);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance r = mock(RiddleInstance.class);
        when(r.isResolved()).thenReturn(true);
        final RiddleInstance r1 = mock(RiddleInstance.class);
        when(r1.isResolved()).thenReturn(false);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Arrays.asList(r, r1));
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);

        assertThat(gameExecutionServiceBean.isActiveWorkflowPartInstanceFinished(1L)).isFalse();
    }

    @Test
    public void testIsActiveWorkflowPartInstanceFinished_NoRiddles() {
        final Game game = mock(Game.class);
        when(gameService.load(1L)).thenReturn(game);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.emptyList());
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        assertThat(gameExecutionServiceBean.isActiveWorkflowPartInstanceFinished(1L)).isTrue();
    }

    @Test
    public void testIsActiveWorkflowPartInstanceFinished_GameNull() {
        when(gameService.load(2L)).thenReturn(null);
        assertThat(gameExecutionServiceBean.isActiveWorkflowPartInstanceFinished(2L)).isFalse();
    }

    @Test
    public void testIsActiveWorkflowPartInstanceFinished_ActivePartNull() {
        final Game game = mock(Game.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(null);
        when(gameService.load(2L)).thenReturn(game);
        assertThat(gameExecutionServiceBean.isActiveWorkflowPartInstanceFinished(2L)).isFalse();
    }

    @Test
    public void testCheckRiddleSolution_Correct_PartFinished() {
        final RiddleInstance r = mock(RiddleInstance.class);
        when(r.isResolved()).thenReturn(true);
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getGameId()).thenReturn("gameID");
        when(gameService.load(1L)).thenReturn(game);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(game.getActiveWorkflowPartInstance().getRiddleInstanceList()).thenReturn(List.of(r));
        when(riddleExecutionService.checkSolution(r, "solution")).thenReturn(true);
        final Riddle riddle = mock(Riddle.class);
        when(r.getRiddle()).thenReturn(riddle);
        when(riddle.getId()).thenReturn(3L);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowTransition wt = mock(WorkflowTransition.class);
        when(workflowPart.getOutgoingTransitions()).thenReturn(Collections.singleton(wt));
        when(workflowPartInstance.getWorkflowPart()).thenReturn(workflowPart);
        final WorkflowPartInstance partInstance2 = mock(WorkflowPartInstance.class);
        when(partInstance2.getWorkflowPart()).thenReturn(workflowPart);
        when(workflowPart.getPartType()).thenReturn(WorkflowPartType.PART);
        when(workflowExecutionService.executeWorkflowTransition(game, wt)).thenReturn(partInstance2);


        assertThat(gameExecutionServiceBean.checkRiddleSolution(1L, r, "solution")).isTrue();
        verify(workflowExecutionService).executeWorkflowTransition(game, wt);
        verify(eventBus).post(any(Object.class));
        verify(gameLockingService, times(2)).lockGame(1L);
        verify(gameLockingService, times(2)).unlockGame(1L);
    }

    @Test
    public void testCheckRiddleSolution_Correct_PartNotFinished() {
        final RiddleInstance r = mock(RiddleInstance.class);
        when(r.isResolved()).thenReturn(true);
        final RiddleInstance r2 = mock(RiddleInstance.class);
        when(r2.isResolved()).thenReturn(false);
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getGameId()).thenReturn("gameID");
        when(gameService.load(1L)).thenReturn(game);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(game.getActiveWorkflowPartInstance().getRiddleInstanceList()).thenReturn(Arrays.asList(r, r2));
        when(riddleExecutionService.checkSolution(r, "solution")).thenReturn(true);
        final Riddle riddle = mock(Riddle.class);
        when(r.getRiddle()).thenReturn(riddle);
        when(riddle.getId()).thenReturn(3L);

        assertThat(gameExecutionServiceBean.checkRiddleSolution(1L, r, "solution")).isTrue();

        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(eventBus).post(any(Object.class));
    }


    @Test
    public void testCheckRiddleSolution_Wrong() {
        final RiddleInstance r = mock(RiddleInstance.class);
        when(riddleExecutionService.checkSolution(r, "solution")).thenReturn(false);

        assertThat(gameExecutionServiceBean.checkRiddleSolution(1L, r, "solution")).isFalse();
        verify(riddleExecutionService).checkSolution(r, "solution");
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
    }

    @Test
    public void testGetNextRiddleHintForRiddleInstance_Success() {
        final RiddleInstance r = mock(RiddleInstance.class);
        final RiddleHint hint = mock(RiddleHint.class);
        final Game game = mock(Game.class);
        when(game.getGameId()).thenReturn("GameID");
        when(gameService.load(1L)).thenReturn(game);
        when(riddleExecutionService.getNextRiddleHint(r)).thenReturn(hint);

        assertThat(gameExecutionServiceBean.getNextRiddleHintForRiddleInstance(1L, r)).isEqualTo(hint);
        verify(eventBus).post(new UpdateDialogEvent("GameID"));
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
    }

    @Test
    public void testGetNextRiddleHintForRiddleInstance_NoHint() {
        final RiddleInstance r = mock(RiddleInstance.class);
        final Game game = mock(Game.class);
        when(gameService.load(1L)).thenReturn(game);
        when(riddleExecutionService.getNextRiddleHint(r)).thenReturn(null);

        assertThat(gameExecutionServiceBean.getNextRiddleHintForRiddleInstance(1L, r)).isNull();
        verifyNoMoreInteractions(eventBus);
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
    }

    @Test
    public void testStartGame() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);

        gameExecutionServiceBean.startGame(game);

        verify(game).setLastStartTime(any(Timestamp.class));
        verify(game).setStarttime(any(Timestamp.class));
        verify(workflowPartInstance).setLastStartTime(any(Timestamp.class));
        verify(workflowPartInstance).setStartTime(any(Timestamp.class));

        verify(gameService).save(game);
        verify(workflowPartInstanceService).save(workflowPartInstance);

        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
    }

    @Test
    public void testPauseGame() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getGameId()).thenReturn("gameID");
        when(game.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 1000000));
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 2000000));

        gameExecutionServiceBean.pauseGame(game);
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowPartInstance).setLastStartTime(null);
        verify(workflowPartInstance).setTotalTime(any(Long.class));
        verify(workflowPartInstance).setEndTime(any(Timestamp.class));
        verify(game).setFinished(false);
        verify(game).setLastStartTime(null);
        verify(game).setTotalTime(any(Long.class));
        verify(game).setEndTime(any(Timestamp.class));
        verify(eventBus).post(new PauseGameEvent("gameID"));
        verify(gameService).save(game);
        verify(workflowPartInstanceService).save(workflowPartInstance);
    }

    @Test
    public void testContinueGame() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getGameId()).thenReturn("gameID");
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);

        gameExecutionServiceBean.continueGame(game);
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowPartInstance).setLastStartTime(any(Timestamp.class));
        verify(game).setLastStartTime(any(Timestamp.class));
        verify(eventBus).post(new ContinueGameEvent("gameID"));
        verify(gameService).save(game);
        verify(workflowPartInstanceService).save(workflowPartInstance);
    }

    @Test
    public void testFinishGame() {
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        when(game.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 1000000));
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 2000000));

        gameExecutionServiceBean.finishGame(game);
        verify(gameLockingService).lockGame(1L);
        verify(gameLockingService).unlockGame(1L);
        verify(workflowPartInstance).setLastStartTime(null);
        verify(workflowPartInstance).setTotalTime(any(Long.class));
        verify(workflowPartInstance).setEndTime(any(Timestamp.class));
        verify(game).setFinished(true);
        verify(game).setLastStartTime(null);
        verify(game).setTotalTime(any(Long.class));
        verify(game).setEndTime(any(Timestamp.class));
        verify(gameService).save(game);
        verify(workflowPartInstanceService).save(workflowPartInstance);
    }

}