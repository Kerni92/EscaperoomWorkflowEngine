package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowPartRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkflowPartServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private WorkflowPartRepository workflowPartRepository;

    @InjectMocks
    private WorkflowPartServiceBean workflowPartServiceBean;

    @Test
    public void testFindWorkflowPartById() {
        final WorkflowPart workflowPart = mock(WorkflowPart.class);
        when(workflowPartRepository.findById(2L)).thenReturn(Optional.of(workflowPart));
        assertThat(workflowPartServiceBean.findWorkflowPartById(2L)).isEqualTo(workflowPart);
    }

    @Test
    public void testFindWorkflowPartById_Id_Null() {
        assertThat(workflowPartServiceBean.findWorkflowPartById(null)).isNull();
        Mockito.verifyNoInteractions(workflowPartRepository);
    }

}