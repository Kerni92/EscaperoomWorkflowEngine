package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleHintRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RiddleHintServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private RiddleHintRepository riddleHintRepository;

    @InjectMocks
    private RiddleHintServiceBean riddleHintServiceBean;


    @Test
    public void testLoad() {
        final RiddleHint hint = mock(RiddleHint.class);
        when(riddleHintRepository.findById(2L)).thenReturn(Optional.of(hint));
        assertThat(riddleHintServiceBean.load(2L)).isEqualTo(hint);
    }

    @Test
    public void testFindAllRiddleHintByRiddle() {
        final Riddle riddle = mock(Riddle.class);
        when(riddle.getId()).thenReturn(2L);
        final RiddleHint hint = mock(RiddleHint.class);
        when(riddleHintRepository.findAllRiddleHintByRiddle(2L)).thenReturn(Collections.singletonList(hint));
        assertThat(riddleHintServiceBean.findAllRiddleHintByRiddle(riddle)).isEqualTo(Collections.singletonList(hint));
    }

    @Test
    public void testFindAllRiddleHintByRiddle_Null() {
        assertThat(riddleHintServiceBean.findAllRiddleHintByRiddle(null)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testFindUsedRiddleHintsForRiddleInstance() {
        final RiddleInstance riddle = mock(RiddleInstance.class);
        when(riddle.getId()).thenReturn(2L);
        final RiddleHint hint = mock(RiddleHint.class);
        when(riddleHintRepository.findAllRiddleHintByRiddleInstance(2L)).thenReturn(Collections.singletonList(hint));
        assertThat(riddleHintServiceBean.findUsedRiddleHintsForRiddleInstance(riddle)).isEqualTo(Collections.singletonList(hint));
    }

    @Test
    public void testfindUsedRiddleHintsForRiddleInstance_Null() {
        assertThat(riddleHintServiceBean.findUsedRiddleHintsForRiddleInstance(null)).isEqualTo(Collections.emptyList());
    }


}