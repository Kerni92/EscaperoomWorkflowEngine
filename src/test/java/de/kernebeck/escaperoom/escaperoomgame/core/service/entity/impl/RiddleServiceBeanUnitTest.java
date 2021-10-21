package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RiddleServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private RiddleRepository riddleRepository;

    @InjectMocks
    private RiddleServiceBean riddleServiceBean;

    @Test
    public void testFindById() {
        final Riddle riddle = mock(Riddle.class);
        when(riddleRepository.findById(2L)).thenReturn(Optional.of(riddle));
        assertThat(riddleServiceBean.findById(2L)).isEqualTo(riddle);
    }

}