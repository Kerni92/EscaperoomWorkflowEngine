package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.RiddleDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.WorkflowDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.WorkflowPartDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.WorkflowTransitionDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class EscaperoomGameImportServiceBean implements EscaperoomGameImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EscaperoomGameImportServiceBean.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowPartRepository workflowPartRepository;

    @Autowired
    private WorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    private RiddleRepository riddleRepository;

    @Autowired
    private RiddleHintRepository riddleHintRepository;


    public boolean createEscaperoomGameFromFile(File file) throws Exception {
        try {
            final WorkflowDTO importGame = OBJECT_MAPPER.readValue(file, WorkflowDTO.class);

            //first create workflowobject
            Workflow workflow = new Workflow();
            workflow.setName(importGame.getName());
            workflow = workflowRepository.save(workflow);

            final Map<String, WorkflowPart> linkIdentifierToWorkflowpartMap = new LinkedHashMap<>();
            //second create workflowparts
            for (final WorkflowPartDTO workflowPartDTO : importGame.getWorkflowParts()) {
                WorkflowPart wp = new WorkflowPart(workflowPartDTO.getName(), workflowPartDTO.getDescription(), WorkflowPartType.fromEnumerationValue(workflowPartDTO.getType()),
                        workflow, Collections.emptyList(), Collections.emptyList());
                wp = workflowPartRepository.save(wp);
                linkIdentifierToWorkflowpartMap.put(workflowPartDTO.getLinkIdentifier(), wp);

                //create riddles and riddle hints
                if (workflowPartDTO.getRiddles() != null && !workflowPartDTO.getRiddles().isEmpty()) {
                    for (final RiddleDTO riddleDTO : workflowPartDTO.getRiddles()) {
                        Riddle riddle = new Riddle(riddleDTO.getName(), riddleDTO.getSortIndex(), riddleDTO.getContent(), Collections.emptyList());
                        riddle.setWorkflowPart(wp);
                        riddle = riddleRepository.save(riddle);

                        //create riddle hints if existing
                        if (riddleDTO.getHints() != null && !riddleDTO.getHints().isEmpty()) {
                            final RiddleHint riddleHint = new RiddleHint(riddleDTO.getName(), riddleDTO.getContent(), riddle);
                            riddleHintRepository.save(riddleHint);
                        }
                    }
                }
            }

            //third create transitions
            for (final WorkflowTransitionDTO transitionDTO : importGame.getWorkflowTransitions()) {
                //validate
                if (!linkIdentifierToWorkflowpartMap.containsKey(transitionDTO.getLinkIdentifierSourceWorkflowPart()) || !linkIdentifierToWorkflowpartMap.containsKey(transitionDTO.getLinkIdentifierTargetWorkflowPart())) {
                    throw new Exception(""); //todo build specific exception
                }
                final WorkflowTransition transition = new WorkflowTransition(transitionDTO.getName(), transitionDTO.getDescription(), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierSourceWorkflowPart()), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierTargetWorkflowPart()));
                workflowTransitionRepository.save(transition);
            }
        }
        catch (IOException e) {
            LOGGER.error("Import game of file " + file.getName() + " failed!", e);
        }
        return true;

    }
}