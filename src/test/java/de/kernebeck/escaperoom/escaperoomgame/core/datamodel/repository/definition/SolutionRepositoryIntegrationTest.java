package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.WorkflowPart;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private RiddleRepository riddleRepository;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowPartRepository workflowPartRepository;


    private Riddle riddle;

    @BeforeEach
    public void setup() {
        final Workflow w = workflowRepository.saveAndFlush(new Workflow("blub", null));
        final WorkflowPart wp = workflowPartRepository.saveAndFlush(new WorkflowPart("blub", "blub", WorkflowPartType.PART, w, Collections.emptyList(), Collections.emptySet(), Collections.emptySet()));
        Riddle r = new Riddle("blub2", 0, "", Collections.emptyList());
        r.setWorkflowPart(wp);
        riddleRepository.saveAndFlush(r);
        this.riddle = r;

        solutionRepository.saveAndFlush(new Solution("blub", "blub", SolutionType.TEXT, "blub", r));
        solutionRepository.saveAndFlush(new Solution("blub2", "blub2", SolutionType.TEXT, "blu2b", r));

    }

    @Test
    public void testFindByRiddle() {
        assertThat(solutionRepository.findByRiddle(riddle)).hasSize(2);
    }

    @AfterEach
    public void tearDown() {
        workflowRepository.deleteAll();

        workflowRepository.flush();
    }


}