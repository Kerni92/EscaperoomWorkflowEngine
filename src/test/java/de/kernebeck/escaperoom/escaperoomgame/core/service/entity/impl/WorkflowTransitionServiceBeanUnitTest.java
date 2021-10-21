package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowTransitionRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkflowTransitionServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private WorkflowTransitionRepository workflowTransitionRepository;

    @Mock
    private WorkflowPartInstanceService workflowPartInstanceService;

    @InjectMocks
    private WorkflowTransitionServiceBean workflowTransitionServiceBean;

    @Test
    public void testFindById() {
        final WorkflowTransition transition = mock(WorkflowTransition.class);
        when(workflowTransitionRepository.findById(2L)).thenReturn(Optional.of(transition));
        assertThat(workflowTransitionServiceBean.findById(2L)).isEqualTo(transition);
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstance() {
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.isResolved()).thenReturn(true);
        when(workflowPartInstance.getId()).thenReturn(3L);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        when(workflowTransitionRepository.findByWorkflowPartInstanceAndOrderBySortIndex(3L)).thenReturn(Collections.singletonList(workflowTransition));
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstance(workflowPartInstance)).isEqualTo(Collections.singletonList(workflowTransition));
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstance_NotAllRiddlesSolved() {
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.isResolved()).thenReturn(true);
        when(workflowPartInstance.getId()).thenReturn(3L);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstance(workflowPartInstance)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstance_PartNull() {
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstance(null)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstanceId() {
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.isResolved()).thenReturn(true);
        when(workflowPartInstance.getId()).thenReturn(3L);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        when(workflowPartInstanceService.findWorkflowPartInstanceById(3L)).thenReturn(workflowPartInstance);
        final WorkflowTransition workflowTransition = mock(WorkflowTransition.class);
        when(workflowTransitionRepository.findByWorkflowPartInstanceAndOrderBySortIndex(3L)).thenReturn(Collections.singletonList(workflowTransition));
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstanceId(3L)).isEqualTo(Collections.singletonList(workflowTransition));
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstanceId_NotAllRiddlesSolved() {
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.isResolved()).thenReturn(true);
        when(workflowPartInstance.getId()).thenReturn(3L);
        when(workflowPartInstance.getRiddleInstanceList()).thenReturn(Collections.singletonList(riddleInstance));
        when(workflowPartInstanceService.findWorkflowPartInstanceById(3L)).thenReturn(workflowPartInstance);
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstanceId(3L)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testFindWorkflowTransitionsByWorkflowPartInstanceId_PartNull() {
        when(workflowPartInstanceService.findWorkflowPartInstanceById(3L)).thenReturn(null);
        assertThat(workflowTransitionServiceBean.findWorkflowTransitionsByWorkflowPartInstanceId(3L)).isEqualTo(Collections.emptyList());
    }

}