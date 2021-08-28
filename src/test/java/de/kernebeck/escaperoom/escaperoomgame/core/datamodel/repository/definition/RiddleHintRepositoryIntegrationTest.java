package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.GameRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class RiddleHintRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RiddleHintRepository riddleHintRepository;

    @Autowired
    private RiddleRepository riddleRepository;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowPartRepository workflowPartRepository;

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Autowired
    private GameRepository gameRepository;

    private Long riddleId;
    private Long riddleInstanceId;

    @BeforeEach
    public void setup() {
        final Workflow w = workflowRepository.saveAndFlush(new Workflow("blub", null));
        final WorkflowPart wp = workflowPartRepository.saveAndFlush(new WorkflowPart("blub", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));
        Riddle r = new Riddle("blub2", 0, "", Collections.emptyList());
        r.setWorkflowPart(wp);
        riddleRepository.saveAndFlush(r);
        this.riddleId = r.getId();

        final RiddleHint hint = new RiddleHint("blub", "blub", 0, r);
        riddleHintRepository.save(hint);

        Game g2 = new Game("testGameIdNotFinished", null, null, null, Boolean.FALSE, null, w);
        g2.setUsernames(Collections.singletonList("blub"));
        g2.setExecutedWorkflowParts(Collections.emptyList());
        g2 = gameRepository.saveAndFlush(g2);
        final WorkflowPartInstance workflowPartInstance = workflowPartInstanceRepository.saveAndFlush(new WorkflowPartInstance(wp, null, null, null, null, g2));
        riddleInstanceId = riddleInstanceRepository.saveAndFlush(new RiddleInstance(r, Collections.singletonList(hint), workflowPartInstance, 0, false)).getId();

    }

    @Test
    public void testFindAllRiddleHintByRiddle() {
        assertThat(riddleHintRepository.findAllRiddleHintByRiddle(riddleId)).hasSize(1);
        assertThat(riddleHintRepository.findAllRiddleHintByRiddle(20000L)).isEmpty();
    }

    @Test
    public void testFindAllRiddleHintByRiddleInstance() {
        assertThat(riddleHintRepository.findAllRiddleHintByRiddleInstance(riddleInstanceId)).hasSize(1);
        assertThat(riddleHintRepository.findAllRiddleHintByRiddleInstance(20000L)).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        workflowRepository.deleteAll();
        workflowRepository.flush();
    }
}