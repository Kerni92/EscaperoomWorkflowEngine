package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.GameRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

class GameServiceBeanUnitTest extends AbstractUnitTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Mock
    private RiddleHintService riddleHintService;

    @InjectMocks
    private GameServiceBean gameServiceBean;


    @Test
    public void testCreateGame_NoWorkflowFound() {
        when(workflowService.findById(2L)).thenReturn(Optional.empty());
        gameServiceBean.createGame(2L, Arrays.asList("Test"));
        verifyNoInteractions(gameRepository);
    }

    @Test
    public void testCreateGame_Success() {
        final Workflow workflow = mock(Workflow.class);
        final WorkflowPart part = mock(WorkflowPart.class);
        final WorkflowPartInstance partInstance = mock(WorkflowPartInstance.class);
        when(gameRepository.save(any(Game.class))).then(returnsFirstArg());
        when(workflow.getWorkflowStart()).thenReturn(part);

        when(workflowService.findById(1L)).thenReturn(Optional.of(workflow));
        when(workflowPartInstanceService.createWorkflowPartInstanceFromWorkflowPart(any(), any())).thenReturn(partInstance);

        final Game result = gameServiceBean.createGame(1L, Arrays.asList("test", "test2"));
        verify(gameRepository, times(2)).save(any(Game.class));
        assertThat(result.getGameId()).isNotEmpty();
        assertThat(result.getActiveWorkflowPartInstance()).isEqualTo(partInstance);
        assertThat(result.getUsernames()).isEqualTo(Arrays.asList("test", "test2"));
    }

    @Test
    public void testFindByGameId_IdNull() {
        assertThat(gameServiceBean.findByGameId(null)).isNull();
    }

    @Test
    public void testFindByGameId_NoGameFound() {
        when(gameRepository.findByGameId("test")).thenReturn(Optional.empty());
        assertThat(gameServiceBean.findByGameId("test")).isNull();
    }

    @Test
    public void testFindByGameId_GameFound() {
        final Game game = mock(Game.class);
        when(gameRepository.findByGameId("test")).thenReturn(Optional.of(game));
        assertThat(gameServiceBean.findByGameId("test")).isEqualTo(game);
    }

    @Test
    public void testFindGameByRiddleInstance_IdNull() {
        assertThat(gameServiceBean.findByGameId(null)).isNull();
    }

    @Test
    public void testFindGameByRiddleInstance_NoGameFound() {
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.getId()).thenReturn(1L);
        when(gameRepository.findGameByRiddleInstance(1L)).thenReturn(Optional.empty());
        assertThat(gameServiceBean.findGameByRiddleInstance(riddleInstance)).isNull();
    }

    @Test
    public void testFindGameByRiddleInstance_GameFound() {
        final Game game = mock(Game.class);
        final RiddleInstance riddleInstance = mock(RiddleInstance.class);
        when(riddleInstance.getId()).thenReturn(3L);
        when(gameRepository.findGameByRiddleInstance(3L)).thenReturn(Optional.of(game));
        assertThat(gameServiceBean.findGameByRiddleInstance(riddleInstance)).isEqualTo(game);
    }

    @Test
    public void testFindNotFinishedByGameId_Found() {
        final Game game = mock(Game.class);
        when(gameRepository.findByGameIdAndFinishedFalse("test")).thenReturn(Optional.of(game));
        assertThat(gameServiceBean.findNotFinishedByGameId("test")).isEqualTo(game);
    }

    @Test
    public void testFindNotFinishedByGameId_NotFound() {
        when(gameRepository.findByGameIdAndFinishedFalse("test")).thenReturn(Optional.empty());
        assertThat(gameServiceBean.findNotFinishedByGameId("test")).isNull();
    }

    @Test
    public void testFindRunningGames() {
        final Game game = mock(Game.class);
        final Game game2 = mock(Game.class);
        final Game game3 = mock(Game.class);
        when(game.getStarttime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(game.getEndTime()).thenReturn(null);
        when(game2.getStarttime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(game2.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(game2.getEndTime()).thenReturn(new Timestamp(System.currentTimeMillis() - 20000000));
        when(game3.getStarttime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(game3.getLastStartTime()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(game3.getEndTime()).thenReturn(new Timestamp(System.currentTimeMillis() + 20000000));

        when(gameRepository.findGamesByFinishedFalse()).thenReturn(Arrays.asList(game, game2, game3));

        assertThat(gameServiceBean.findRunningGames()).isEqualTo(Arrays.asList(game, game2));
    }

    @Test
    public void testLoad() {
        final Game game = mock(Game.class);
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));
        assertThat(gameServiceBean.load(2L)).isEqualTo(game);
    }

    @Test
    public void testLoad_NotExisiting() {
        when(gameRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(gameServiceBean.load(2L)).isNull();
    }

    @Test
    public void testSave() {
        final Game game = mock(Game.class);
        gameServiceBean.save(game);
        verify(gameRepository).save(game);
    }

    @Test
    public void testSave_GameNull() {
        gameServiceBean.save(null);
        verifyNoInteractions(gameRepository);
    }
}