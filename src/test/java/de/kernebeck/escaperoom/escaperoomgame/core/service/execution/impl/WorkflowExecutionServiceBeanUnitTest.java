package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.UpdateUIEvent;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WorkflowExecutionServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private GameService gameService;

    @Mock
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private WorkflowExecutionServiceBean workflowExecutionServiceBean;


    @Test
    public void testIsTransitionExecutionPossible_True() {
        final Game game = mock(Game.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowTransition workflowTransitionToCheck = mock(WorkflowTransition.class);
        when(workflowTransition.getId()).thenReturn(1L);
        when(workflowTransitionToCheck.getId()).thenReturn(1L);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(workflowPart);
        when(workflowPart.getOutgoingTransitions()).thenReturn(Collections.singleton(workflowTransition));

        assertThat(workflowExecutionServiceBean.isTransitionExecutionPossible(game, workflowTransitionToCheck)).isTrue();
    }

    @Test
    public void testIsTransitionExecutionPossible_False() {
        final Game game = mock(Game.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowTransition workflowTransitionToCheck = mock(WorkflowTransition.class);
        when(workflowTransition.getId()).thenReturn(1L);
        when(workflowTransitionToCheck.getId()).thenReturn(2L);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(workflowPart);
        when(workflowPart.getOutgoingTransitions()).thenReturn(Collections.singleton(workflowTransition));

        assertThat(workflowExecutionServiceBean.isTransitionExecutionPossible(game, workflowTransitionToCheck)).isFalse();
    }

    @Test
    public void testExecuteWorkflowTransition() {
        final Game game = mock(Game.class);
        when(game.getGameId()).thenReturn("gameID");
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPartInstance nextWorkflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowPart destinationPart = mock(WorkflowPart.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(nextWorkflowPartInstance.getWorkflowPart()).thenReturn(destinationPart);
        when(destinationPart.getPartType()).thenReturn(WorkflowPartType.PART);
        when(nextWorkflowPartInstance.getId()).thenReturn(4L);
        when(workflowPartInstanceService.findWorkflowPartInstanceById(4L)).thenReturn(nextWorkflowPartInstance);
        when(nextWorkflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        when(workflowTransition.getDestinationPart()).thenReturn(destinationPart);
        when(workflowTransition.getId()).thenReturn(1L);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(workflowPart);
        when(workflowPart.getOutgoingTransitions()).thenReturn(Collections.singleton(workflowTransition));
        when(workflowPartInstance.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 200000));
        when(workflowPartInstanceService.createWorkflowPartInstanceFromWorkflowPart(eq(game), eq(destinationPart), any(Timestamp.class))).thenReturn(nextWorkflowPartInstance);


        assertThat(workflowExecutionServiceBean.executeWorkflowTransition(game, workflowTransition)).isEqualTo(nextWorkflowPartInstance);
        verify(eventBus).post(new UpdateUIEvent("gameID", null));
        verify(workflowPartInstance).setEndTime(any(Timestamp.class));
        verify(workflowPartInstance).setTotalTime(any(Long.class));
        verify(workflowPartInstanceService).save(workflowPartInstance);
        verify(game).setCurrentWorkflowpart(nextWorkflowPartInstance);
        verify(gameService).save(game);
    }

    @Test
    public void testExecuteWorkflowTransition_NextTransitionIsEmpty() {
        final Game game = mock(Game.class);
        when(game.getGameId()).thenReturn("gameID");
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPartInstance nextWorkflowPartInstance = mock(WorkflowPartInstance.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        final WorkflowPart destinationPart = mock(WorkflowPart.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(nextWorkflowPartInstance.getWorkflowPart()).thenReturn(destinationPart);
        when(destinationPart.getPartType()).thenReturn(WorkflowPartType.PART);
        when(nextWorkflowPartInstance.getId()).thenReturn(4L);
        when(workflowPartInstanceService.findWorkflowPartInstanceById(4L)).thenReturn(nextWorkflowPartInstance);
        when(nextWorkflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        when(workflowTransition.getDestinationPart()).thenReturn(destinationPart);
        when(workflowTransition.getId()).thenReturn(1L);
        when(game.getActiveWorkflowPartInstance()).thenReturn(workflowPartInstance);
        when(workflowPartInstance.getWorkflowPart()).thenReturn(workflowPart);
        when(workflowPart.getOutgoingTransitions()).thenReturn(Collections.singleton(workflowTransition));
        when(workflowPartInstance.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 200000));
        when(workflowPartInstanceService.createWorkflowPartInstanceFromWorkflowPart(eq(game), eq(destinationPart), any(Timestamp.class))).thenReturn(nextWorkflowPartInstance);


        assertThat(workflowExecutionServiceBean.executeWorkflowTransition(game, workflowTransition)).isEqualTo(nextWorkflowPartInstance);
        verify(eventBus).post(new UpdateUIEvent("gameID", null));
        verify(workflowPartInstance).setEndTime(any(Timestamp.class));
        verify(workflowPartInstance).setTotalTime(any(Long.class));
        verify(workflowPartInstanceService).save(workflowPartInstance);
        verify(game).setCurrentWorkflowpart(nextWorkflowPartInstance);
        verify(gameService).save(game);
    }
}