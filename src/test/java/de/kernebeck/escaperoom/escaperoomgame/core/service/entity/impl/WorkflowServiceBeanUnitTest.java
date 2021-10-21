package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkflowServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowServiceBean workflowServiceBean;

    @Test
    public void testFindAll() {
        final Workflow workflow = mock(Workflow.class);
        when(workflow.getId()).thenReturn(1L);
        final Workflow workflow2 = mock(Workflow.class);
        when(workflow2.getId()).thenReturn(2L);
        final Workflow workflow3 = mock(Workflow.class);
        when(workflow3.getId()).thenReturn(3L);
        when(workflowRepository.findAll()).thenReturn(Arrays.asList(workflow2, workflow3, workflow));
        assertThat(workflowServiceBean.findAll()).isEqualTo(Arrays.asList(workflow, workflow2, workflow3));
    }

    @Test
    public void testFindById() {
        final Workflow workflow = mock(Workflow.class);
        when(workflowRepository.findById(2L)).thenReturn(Optional.of(workflow));
        assertThat(workflowServiceBean.findById(2L)).isEqualTo(Optional.of(workflow));
    }
}