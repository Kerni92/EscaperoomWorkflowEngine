package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowTransition;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.GameRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowTransitionRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowPartRepository workflowPartRepository;

    @Autowired
    private GameRepository gameRepository;

    private Long sourceWPId;

    @BeforeEach
    public void setup() {
        final Workflow w = workflowRepository.saveAndFlush(new Workflow("blub", null));
        final WorkflowPart wp = workflowPartRepository.saveAndFlush(new WorkflowPart("blub", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));
        final WorkflowPart wp2 = workflowPartRepository.saveAndFlush(new WorkflowPart("blub2", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));
        final WorkflowPart wp3 = workflowPartRepository.saveAndFlush(new WorkflowPart("blub3", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));

        workflowTransitionRepository.saveAndFlush(new WorkflowTransition("t1", "blub", 0, wp, wp2));
        workflowTransitionRepository.saveAndFlush(new WorkflowTransition("t2", "blub", 1, wp, wp3));

        Game g2 = new Game("testGameIdNotFinished", null, null, null, Boolean.FALSE, null, w);
        g2.setUsernames(Collections.singletonList("blub"));
        g2.setExecutedWorkflowParts(Collections.emptyList());
        g2 = gameRepository.saveAndFlush(g2);
        final WorkflowPartInstance workflowPartInstance = workflowPartInstanceRepository.saveAndFlush(new WorkflowPartInstance(wp, null, null, null, null, g2));
        sourceWPId = workflowPartInstance.getId();
    }

    @Test
    public void testFindByWorkflowPartInstanceAndOrderBySortIndex() {
        final List<WorkflowTransition> result = workflowTransitionRepository.findByWorkflowPartInstanceAndOrderBySortIndex(sourceWPId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("t1");
        assertThat(result.get(1).getName()).isEqualTo("t2");

        assertThat(workflowTransitionRepository.findByWorkflowPartInstanceAndOrderBySortIndex(3213131L)).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        workflowPartInstanceRepository.deleteAll();
        gameRepository.deleteAll();
        workflowTransitionRepository.deleteAll();
        workflowPartRepository.deleteAll();
        workflowRepository.deleteAll();

        workflowTransitionRepository.flush();
        gameRepository.flush();
        workflowTransitionRepository.flush();
        workflowPartRepository.flush();
        workflowRepository.flush();
    }

}