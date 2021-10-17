package de.kernebeck.escaperoom.escaperoomgame.core.service.execution.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RiddleExecutionServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private GameService gameService;

    @Mock
    private RiddleInstanceService riddleInstanceService;

    @Mock
    private RiddleHintService riddleHintService;

    @Mock
    private SolutionService solutionService;

    @InjectMocks
    private RiddleExecutionServiceBean riddleExecutionServiceBean;


    @Test
    public void testCheckSolution_SolutionCorrect() {
        final Game game = mock(Game.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        final Riddle riddle = mock(Riddle.class);
        final Solution solution = mock(Solution.class);
        when(riddle.getId()).thenReturn(3L);
        when(gameService.findGameByRiddleInstance(riddleInstance)).thenReturn(game);
        when(riddleInstance.getRiddle()).thenReturn(riddle);
        when(solutionService.findByRiddleId(3L)).thenReturn(Arrays.asList(solution));
        when(solution.getSolution()).thenReturn("solution");


        assertThat(riddleExecutionServiceBean.checkSolution(riddleInstance, "solution")).isTrue();
        verify(riddleInstance).setAttempts(1);
        verify(riddleInstance).setResolved(true);
        verify(riddleInstanceService).save(riddleInstance);
    }

    @Test
    public void testCheckSolution_SolutionWrong() {
        final Game game = mock(Game.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.getAttempts()).thenReturn(2);
        final Riddle riddle = mock(Riddle.class);
        final Solution solution = mock(Solution.class);
        when(riddle.getId()).thenReturn(3L);
        when(gameService.findGameByRiddleInstance(riddleInstance)).thenReturn(game);
        when(riddleInstance.getRiddle()).thenReturn(riddle);
        when(solutionService.findByRiddleId(3L)).thenReturn(Arrays.asList(solution));
        when(solution.getSolution()).thenReturn("solution");

        assertThat(riddleExecutionServiceBean.checkSolution(riddleInstance, "solution2")).isFalse();
        verify(riddleInstance).setAttempts(3);
        verify(riddleInstanceService).save(riddleInstance);
        verifyNoMoreInteractions(riddleInstance);
    }

    @Test
    public void testCheckSolution_NoGameFound() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(gameService.findGameByRiddleInstance(riddleInstance)).thenReturn(null);
        assertThat(riddleExecutionServiceBean.checkSolution(riddleInstance, "solution2")).isFalse();
    }

    @Test
    public void testGetNextRiddleHint_FirstOne() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.getId()).thenReturn(2L);
        final RiddleHint nextHint = mock(RiddleHint.class);
        when(riddleInstanceService.findById(2L)).thenReturn(riddleInstance);
        final Riddle riddle = mock(Riddle.class);
        when(riddleInstance.getRiddle()).thenReturn(riddle);

        when(riddleHintService.findAllRiddleHintByRiddle(riddle)).thenReturn(Collections.singletonList(nextHint));
        when(riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstance)).thenReturn(Collections.emptyList());

        assertThat(riddleExecutionServiceBean.getNextRiddleHint(riddleInstance)).isEqualTo(nextHint);
        verify(riddleInstance).addUsedHint(nextHint);
        verify(riddleInstanceService).save(riddleInstance);
    }

    @Test
    public void testGetNextRiddleHint_AllUsed() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        final RiddleHint nextHint = mock(RiddleHint.class);
        final Riddle riddle = mock(Riddle.class);
        when(riddleInstance.getRiddle()).thenReturn(riddle);

        when(riddleHintService.findAllRiddleHintByRiddle(riddle)).thenReturn(Collections.singletonList(nextHint));
        when(riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstance)).thenReturn(Collections.singletonList(nextHint));

        assertThat(riddleExecutionServiceBean.getNextRiddleHint(riddleInstance)).isNull();
        verifyNoInteractions(riddleInstanceService);
    }

    @Test
    public void testGetNextRiddleHint_OneUsed() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.getId()).thenReturn(2L);
        final RiddleHint usedHint = mock(RiddleHint.class);
        final RiddleHint nextHint = mock(RiddleHint.class);
        when(usedHint.getSortIndex()).thenReturn(0);
        when(nextHint.getSortIndex()).thenReturn(1);
        when(usedHint.getId()).thenReturn(1L);
        when(nextHint.getId()).thenReturn(3L);
        when(riddleInstanceService.findById(2L)).thenReturn(riddleInstance);
        final Riddle riddle = mock(Riddle.class);
        when(riddleInstance.getRiddle()).thenReturn(riddle);

        when(riddleHintService.findAllRiddleHintByRiddle(riddle)).thenReturn(Arrays.asList(nextHint, usedHint));
        when(riddleHintService.findUsedRiddleHintsForRiddleInstance(riddleInstance)).thenReturn(Collections.singletonList(usedHint));

        assertThat(riddleExecutionServiceBean.getNextRiddleHint(riddleInstance)).isEqualTo(nextHint);
        verify(riddleInstance).addUsedHint(nextHint);
        verify(riddleInstanceService).save(riddleInstance);
    }

}