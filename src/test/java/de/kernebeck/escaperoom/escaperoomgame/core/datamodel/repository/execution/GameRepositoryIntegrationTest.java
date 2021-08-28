package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowPartRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class GameRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RiddleRepository riddleRepository;

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Autowired
    private WorkflowPartRepository workflowPartRepository;

    private Long riddleInstanceId;

    private Long gameId;

    @BeforeEach
    public void setup() {
        final Workflow w = workflowRepository.saveAndFlush(new Workflow("blub", null));
        final WorkflowPart wp = workflowPartRepository.saveAndFlush(new WorkflowPart("blub", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));
        Riddle r = new Riddle("blub2", 0, "", Collections.emptyList());
        r.setWorkflowPart(wp);
        riddleRepository.saveAndFlush(r);
        Game g1 = new Game("testGameIdFinished", null, null, null, Boolean.TRUE, null, w);
        g1.setExecutedWorkflowParts(Collections.emptyList());
        g1.setUsernames(Collections.singletonList("blub"));
        gameRepository.saveAndFlush(g1);

        Game g2 = new Game("testGameIdNotFinished", null, null, null, Boolean.FALSE, null, w);
        g2.setUsernames(Collections.singletonList("blub"));
        g2.setExecutedWorkflowParts(Collections.emptyList());
        g2 = gameRepository.saveAndFlush(g2);
        gameId = g2.getId();
        final WorkflowPartInstance workflowPartInstance = workflowPartInstanceRepository.saveAndFlush(new WorkflowPartInstance(wp, null, null, null, null, g2));
        riddleInstanceId = riddleInstanceRepository.saveAndFlush(new RiddleInstance(r, Collections.emptyList(), workflowPartInstance, 0, false)).getId();
    }

    @Test
    void testFindByGameId() {
        assertThat(gameRepository.findByGameId("testGameIdFinished")).isNotEmpty();
        assertThat(gameRepository.findByGameId("blub")).isEmpty();
    }

    @Test
    void testFindByGameIdAndFinishedFalse() {
        assertThat(gameRepository.findByGameIdAndFinishedFalse("testGameIdNotFinished")).isNotEmpty();
        assertThat(gameRepository.findByGameIdAndFinishedFalse("testGameIdFinished")).isEmpty();
        assertThat(gameRepository.findByGameId("blub")).isEmpty();
    }

    @Test
    void testFindGameByRiddleInstance() {
        assertThat(gameRepository.findGameByRiddleInstance(riddleInstanceId)).isNotEmpty();
        final Game g = gameRepository.findGameByRiddleInstance(riddleInstanceId).get();
        assertThat(g.getId()).isEqualTo(gameId);
        assertThat(gameRepository.findGameByRiddleInstance(7897L)).isEmpty();
    }

    @Test
    void testFindGamesByFinishedFalse() {
        assertThat(gameRepository.findGamesByFinishedFalse()).hasSize(1);
    }

    @AfterEach
    public void tearDown() {
        workflowRepository.deleteAll();
        workflowRepository.flush();
    }
}