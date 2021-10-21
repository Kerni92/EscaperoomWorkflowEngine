package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RiddleInstanceServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private RiddleInstanceRepository riddleInstanceRepository;

    @InjectMocks
    private RiddleInstanceServiceBean riddleInstanceServiceBean;

    @Test
    public void findById() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstanceRepository.findById(2L)).thenReturn(Optional.of(riddleInstance));
        assertThat(riddleInstanceServiceBean.findById(2L)).isEqualTo(riddleInstance);
    }

    @Test
    public void save() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        riddleInstanceServiceBean.save(riddleInstance);
        verify(riddleInstanceRepository).save(riddleInstance);
    }

    @Test
    public void save_Null() {
        riddleInstanceServiceBean.save(null);
        verifyNoInteractions(riddleInstanceRepository);
    }


    @Test
    public void createRiddleInstanceByRiddle() {
        final Riddle riddle = mock(Riddle.class);
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        //todo check if possible implementation of equals has bad effects, if not comment in
//        final RiddleInstance verificationObject = new RiddleInstance(riddle, Collections.emptyList(), workflowPartInstance, 0, Boolean.FALSE);
        riddleInstanceServiceBean.createRiddleInstanceByRiddle(riddle, workflowPartInstance);
//        verify(riddleInstanceRepository).save(verificationObject);
        verify(riddleInstanceRepository).save(any(RiddleInstance.class));
    }

    @Test
    public void createRiddleInstanceByRiddle_WPINull() {
        final Riddle riddle = mock(Riddle.class);
        riddleInstanceServiceBean.createRiddleInstanceByRiddle(riddle, null);
        verifyNoInteractions(riddleInstanceRepository);
    }

    @Test
    public void createRiddleInstanceByRiddle_RiddleNull() {
        final WorkflowPartInstance workflowPartInstance = mock(WorkflowPartInstance.class);
        riddleInstanceServiceBean.createRiddleInstanceByRiddle(null, workflowPartInstance);
        verifyNoInteractions(riddleInstanceRepository);

    }
}