package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.SolutionRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SolutionServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private RiddleService riddleService;

    @InjectMocks
    private SolutionServiceBean solutionServiceBean;

    @Test
    public void testFindByRiddleId() {
        final Riddle riddle = mock(Riddle.class);
        final Solution solution = mock(Solution.class);
        when(riddleService.findById(2L)).thenReturn(riddle);
        when(solutionRepository.findByRiddle(riddle)).thenReturn(Collections.singletonList(solution));
        assertThat(solutionServiceBean.findByRiddleId(2L)).isEqualTo(Collections.singletonList(solution));
    }

    @Test
    public void testFindByRiddleId_No_Solutions_Found() {
        final Riddle riddle = mock(Riddle.class);
        final Solution solution = mock(Solution.class);
        when(riddleService.findById(2L)).thenReturn(riddle);
        when(solutionRepository.findByRiddle(riddle)).thenReturn(null);
        assertThat(solutionServiceBean.findByRiddleId(2L)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testFindByRiddleId_RiddleNotFound() {
        when(riddleService.findById(2L)).thenReturn(null);
        assertThat(solutionServiceBean.findByRiddleId(2L)).isEqualTo(Collections.emptyList());
    }


}