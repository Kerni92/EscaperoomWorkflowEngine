package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@Transactional
public class EscaperoomGameImportServiceBean implements EscaperoomGameImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EscaperoomGameImportServiceBean.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WorkflowRepository workflowRepository;

    private final WorkflowPartRepository workflowPartRepository;

    private final WorkflowTransitionRepository workflowTransitionRepository;

    private final RiddleRepository riddleRepository;

    private final RiddleHintRepository riddleHintRepository;

    private final SolutionRepository solutionRepository;

    @Autowired
    public EscaperoomGameImportServiceBean(WorkflowRepository workflowRepository, WorkflowPartRepository workflowPartRepository, WorkflowTransitionRepository workflowTransitionRepository, RiddleRepository riddleRepository, RiddleHintRepository riddleHintRepository, SolutionRepository solutionRepository) {
        this.workflowRepository = workflowRepository;
        this.workflowPartRepository = workflowPartRepository;
        this.workflowTransitionRepository = workflowTransitionRepository;
        this.riddleRepository = riddleRepository;
        this.riddleHintRepository = riddleHintRepository;
        this.solutionRepository = solutionRepository;
    }

    @Override
    public WorkflowImportResult createEscaperoomgameFromFile(File file) {
        try {
            return createEscaperoomGameFromReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            return new WorkflowImportResult(false, Collections.singletonList("Die angegebene Datei " + file.getAbsolutePath() + " zum importieren existiert auf dem Dateisystem nicht!"));
        }
    }

    @Override
    public WorkflowImportResult createEscaperoomGameFromReader(Reader reader) {
        final List<String> errorMessages = new ArrayList<>();
        Workflow workflow = null;
        try {
            final WorkflowDTO importGame = OBJECT_MAPPER.readValue(reader, WorkflowDTO.class);

            //TODO add more validation
            //first create workflowobject
            workflow = new Workflow();
            workflow.setName(importGame.getName());
            workflow = workflowRepository.save(workflow);

            final Map<String, WorkflowPart> linkIdentifierToWorkflowpartMap = new LinkedHashMap<>();
            //second create workflowparts
            for (final WorkflowPartDTO workflowPartDTO : importGame.getWorkflowParts()) {
                //TODO add more validation
                WorkflowPart wp = new WorkflowPart(workflowPartDTO.getName(), workflowPartDTO.getDescription(), WorkflowPartType.fromEnumerationValue(workflowPartDTO.getType()),
                        workflow, Collections.emptyList(), Collections.emptySet(), Collections.emptySet());
                wp = workflowPartRepository.save(wp);
                linkIdentifierToWorkflowpartMap.put(workflowPartDTO.getLinkIdentifier(), wp);

                if (importGame.getStartPartLinkId().equalsIgnoreCase(workflowPartDTO.getLinkIdentifier())) {
                    workflow.setWorkflowStart(wp);
                    workflowRepository.save(workflow);
                }

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

                //create solutions
                if (workflowPartDTO.getSolutions() != null && !workflowPartDTO.getSolutions().isEmpty()) {
                    for (final SolutionDTO solutionDTO : workflowPartDTO.getSolutions()) {
                        final Solution solution = new Solution(solutionDTO.getName(), solutionDTO.getDescription(), SolutionType.fromEnumerationValue(solutionDTO.getType()), solutionDTO.getSolution(), wp);
                        solution.setSolutionOptions(solutionDTO.getSolutionOptions());
                        solutionRepository.save(solution);
                    }
                }
            }

            //third create transitions
            for (final WorkflowTransitionDTO transitionDTO : importGame.getWorkflowTransitions()) {
                //validate
                if (!linkIdentifierToWorkflowpartMap.containsKey(transitionDTO.getLinkIdentifierSourceWorkflowPart()) || !linkIdentifierToWorkflowpartMap.containsKey(transitionDTO.getLinkIdentifierTargetWorkflowPart())) {
                    //cleanup created objects from db
                    workflowRepository.delete(workflow);
                    return new WorkflowImportResult(false, Collections.singletonList("Es konnte kein erzeugtes Start oder Ziel Workflowpart Objekt für den Startlinkidentifier " + transitionDTO.getLinkIdentifierSourceWorkflowPart() + " oder den Ziellinkidentifer " + transitionDTO.getLinkIdentifierTargetWorkflowPart() + " gefunden werden. Bitte die Importdefinitionen überprüfen."));
                }
                final WorkflowTransition transition = new WorkflowTransition(transitionDTO.getName(), transitionDTO.getDescription(), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierSourceWorkflowPart()), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierTargetWorkflowPart()));
                workflowTransitionRepository.save(transition);
            }

            //set startpart for workflow
            if (workflow.getWorkflowStart() == null) {
                workflowRepository.delete(workflow);
                return new WorkflowImportResult(false, Collections.singletonList("Es wurde kein Startelement für den Workflow gesetzt oder es ist keins angegeben."));
            }

            return new WorkflowImportResult(true);
        }
        catch (IOException e) {
            errorMessages.add("es ist ein unerwarteter Fehler beim Import aufgetreten! Meldung: " + e.getMessage());
            if (workflow != null) { //workflow could be null when converting file json to object fails
                workflowRepository.delete(workflow);
            }
        }
        return new WorkflowImportResult(false, errorMessages);
    }
}