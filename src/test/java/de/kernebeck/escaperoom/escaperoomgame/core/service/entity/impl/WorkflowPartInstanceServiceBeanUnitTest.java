package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

class WorkflowPartInstanceServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Mock
    private RiddleInstanceService riddleInstanceService;

    @InjectMocks
    private WorkflowPartInstanceServiceBean workflowPartInstanceServiceBean;

    @Test
    public void testFindWorkflowPartInstanceById() {
        final WorkflowPartInstance instance = mock(WorkflowPartInstance.class);
        when(workflowPartInstanceRepository.findById(2L)).thenReturn(Optional.of(instance));
        assertThat(workflowPartInstanceServiceBean.findWorkflowPartInstanceById(2L)).isEqualTo(instance);
    }

    @Test
    public void testFindWorkflowPartInstanceById_Null() {
        assertThat(workflowPartInstanceServiceBean.findWorkflowPartInstanceById(null)).isNull();
        verifyNoInteractions(workflowPartInstanceRepository);
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPart() {
        final Game game = mock(Game.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final Riddle r1 = mock(Riddle.class);
        final Riddle r2 = mock(Riddle.class);
        when(workflowPart.getRiddles()).thenReturn(Arrays.asList(r1, r2));
        when(workflowPartInstanceRepository.save(any(WorkflowPartInstance.class))).then(returnsFirstArg());
        when(workflowPartInstanceRepository.findById(null)).thenReturn(Optional.ofNullable(workflowPartInstance));
        assertThat(workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(game, workflowPart, new Timestamp(System.currentTimeMillis()))).isNotNull();
        verify(riddleInstanceService).createRiddleInstanceByRiddle(eq(r1), any(WorkflowPartInstance.class));
        verify(riddleInstanceService).createRiddleInstanceByRiddle(eq(r2), any(WorkflowPartInstance.class));
        verify(workflowPartInstanceRepository).save(any(WorkflowPartInstance.class));
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPart_GameNull() {
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(null, workflowPart, null);
        verifyNoInteractions(workflowPartInstanceRepository);
        verifyNoInteractions(riddleInstanceService);
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPart_PartNull() {
        final Game game = mock(Game.class);
        workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(game, null, null);
        verifyNoInteractions(workflowPartInstanceRepository);
        verifyNoInteractions(riddleInstanceService);
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPartNoTS() {
        final Game game = mock(Game.class);
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final Riddle r1 = mock(Riddle.class);
        final Riddle r2 = mock(Riddle.class);
        when(workflowPart.getRiddles()).thenReturn(Arrays.asList(r1, r2));
        when(workflowPartInstanceRepository.save(any(WorkflowPartInstance.class))).then(returnsFirstArg());
        when(workflowPartInstanceRepository.findById(null)).thenReturn(Optional.ofNullable(workflowPartInstance));

        assertThat(workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(game, workflowPart)).isNotNull();

        verify(riddleInstanceService).createRiddleInstanceByRiddle(eq(r1), any(WorkflowPartInstance.class));
        verify(riddleInstanceService).createRiddleInstanceByRiddle(eq(r2), any(WorkflowPartInstance.class));
        verify(workflowPartInstanceRepository).save(any(WorkflowPartInstance.class));
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPartNoTS_GameNull() {
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(null, workflowPart);
        verifyNoInteractions(workflowPartInstanceRepository);
        verifyNoInteractions(riddleInstanceService);
    }

    @Test
    public void testCreateWorkflowPartInstanceFromWorkflowPartNOTS_PartNull() {
        final Game game = mock(Game.class);
        workflowPartInstanceServiceBean.createWorkflowPartInstanceFromWorkflowPart(game, null);
        verifyNoInteractions(workflowPartInstanceRepository);
        verifyNoInteractions(riddleInstanceService);
    }

    @Test
    public void testSave() {
        final WorkflowPartInstance instance = mock(WorkflowPartInstance.class);
        workflowPartInstanceServiceBean.save(instance);
        verify(workflowPartInstanceRepository).save(instance);
    }

    @Test
    public void testSave_WPNull() {
        workflowPartInstanceServiceBean.save(null);
        verifyNoInteractions(workflowPartInstanceRepository);
    }


}