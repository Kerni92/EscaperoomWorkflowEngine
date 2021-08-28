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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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
            return createEscaperoomGameFromReader(new FileReader(file, StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            return new WorkflowImportResult(false, Collections.singletonList("Die angegebene Datei " + file.getAbsolutePath() + " zum importieren existiert auf dem Dateisystem nicht!"));
        }
    }

    @Override
    public WorkflowImportResult createEscaperoomGameFromReader(Reader reader) {
        final List<String> errorMessages = new ArrayList<>();
        Workflow workflow = null;
        try {
            final WorkflowDTO importGame = OBJECT_MAPPER.readValue(reader, WorkflowDTO.class);

            //first create workflowobject
            workflow = new Workflow();
            workflow.setName(importGame.getName());
            workflow = workflowRepository.saveAndFlush(workflow);

            final Map<String, WorkflowPart> linkIdentifierToWorkflowpartMap = new LinkedHashMap<>();
            final List<WorkflowPart> allWorkflowParts = new ArrayList<>();
            boolean hasEndPart = false;
            //second create workflowparts
            for (final WorkflowPartDTO workflowPartDTO : importGame.getWorkflowParts()) {
                WorkflowPartType partType = WorkflowPartType.fromEnumerationValue(workflowPartDTO.getType());
                if (partType == null) {
                    partType = WorkflowPartType.PART;
                    errorMessages.add("Es ist kein WorkflowPart Typ angegeben worden oder Wert ist ungültig. Gültige Werte: part, decision, endpart");
                }
                if (partType == WorkflowPartType.ENDPART) {
                    hasEndPart = true;
                }

                WorkflowPart wp = new WorkflowPart(workflowPartDTO.getName(), workflowPartDTO.getDescription(), partType,
                        workflow, Collections.emptyList(), Collections.emptySet(), Collections.emptySet());
                wp = workflowPartRepository.saveAndFlush(wp);
                linkIdentifierToWorkflowpartMap.put(workflowPartDTO.getLinkIdentifier(), wp);
                allWorkflowParts.add(wp);

                if (importGame.getStartPartLinkId().equalsIgnoreCase(workflowPartDTO.getLinkIdentifier())) {
                    workflow.setWorkflowStart(wp);
                    workflowRepository.saveAndFlush(workflow);
                }

                //create riddles and riddle hints
                if (partType == WorkflowPartType.PART && workflowPartDTO.getRiddles() != null && !workflowPartDTO.getRiddles().isEmpty()) {
                    for (final RiddleDTO riddleDTO : workflowPartDTO.getRiddles()) {
                        Riddle riddle = new Riddle(riddleDTO.getName(), riddleDTO.getSortIndex(), riddleDTO.getContent(), Collections.emptyList());
                        riddle.setWorkflowPart(wp);
                        riddle = riddleRepository.saveAndFlush(riddle);

                        //create riddle hints if existing
                        if (riddleDTO.getHints() != null && !riddleDTO.getHints().isEmpty()) {
                            for (final RiddleHintDTO riddleHintDTO : riddleDTO.getHints()) {
                                final RiddleHint riddleHint = new RiddleHint(riddleHintDTO.getName(), riddleHintDTO.getContent(), riddleHintDTO.getSortIndex(), riddle);
                                riddleHintRepository.saveAndFlush(riddleHint);
                            }
                        }

                        //create solutions
                        if (riddleDTO.getSolutions() != null && !riddleDTO.getSolutions().isEmpty()) {
                            for (final SolutionDTO solutionDTO : riddleDTO.getSolutions()) {
                                final Solution solution = new Solution(solutionDTO.getName(), solutionDTO.getDescription(), SolutionType.fromEnumerationValue(solutionDTO.getType()), solutionDTO.getSolution(), riddle);
                                solution.setSolutionOptions(solutionDTO.getSolutionOptions());
                                solutionRepository.saveAndFlush(solution);
                            }
                        }
                    }
                }
                else {
                    if ((partType == WorkflowPartType.DECISION || partType == WorkflowPartType.ENDPART) && workflowPartDTO.getRiddles() != null && !workflowPartDTO.getRiddles().isEmpty()) {
                        errorMessages.add("Für den WorkflowPart Typ Decision oder EndPart sind keine Rätsel erlaubt!");
                    }
                    if (partType == WorkflowPartType.PART && (workflowPartDTO.getRiddles() == null || workflowPartDTO.getRiddles().isEmpty())) {
                        errorMessages.add("Für den WorkflowPart Typ Part müssen Rätsel angegeben werden!");
                    }
                }
            }

            final Map<WorkflowPart, Integer> workflowPartToOutgoingTransitionCountMap = new HashMap<>();
            final List<WorkflowPart> workflowPartsWithIngoeingTransitions = new ArrayList<>();
            //third create transitions
            for (final WorkflowTransitionDTO transitionDTO : importGame.getWorkflowTransitions()) {
                final WorkflowPart sourceWorkflowPart = linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierSourceWorkflowPart());
                final WorkflowPart targetWorkflowPart = linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierTargetWorkflowPart());

                //validate
                if (sourceWorkflowPart == null || targetWorkflowPart == null) {
                    errorMessages.add("Für den Sourcelinkidentifier " + transitionDTO.getLinkIdentifierSourceWorkflowPart() + " oder den targetLinkIdentifier " + transitionDTO.getLinkIdentifierTargetWorkflowPart() + "konnte kein WorkflowPart gefunden werden!");
                    continue;
                }

                if (sourceWorkflowPart.getPartType() == WorkflowPartType.ENDPART) {
                    errorMessages.add("Die Transition mit dem Namen " + transitionDTO.getName() + " ist nicht erlaubt, weil der QuellworkflowPart vom Typ Endpart ist!");
                    continue;
                }

                //create and save
                final WorkflowTransition transition = new WorkflowTransition(transitionDTO.getName(), transitionDTO.getDescription(), transitionDTO.getSortIndex(), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierSourceWorkflowPart()), linkIdentifierToWorkflowpartMap.get(transitionDTO.getLinkIdentifierTargetWorkflowPart()));
                workflowTransitionRepository.saveAndFlush(transition);

                //count how many outoging transitions the sourceworkflowpart has
                workflowPartToOutgoingTransitionCountMap.putIfAbsent(sourceWorkflowPart, 0);
                workflowPartToOutgoingTransitionCountMap.put(sourceWorkflowPart, workflowPartToOutgoingTransitionCountMap.get(sourceWorkflowPart) + 1);
                workflowPartsWithIngoeingTransitions.add(targetWorkflowPart);
            }

            for (Map.Entry<WorkflowPart, Integer> entry : workflowPartToOutgoingTransitionCountMap.entrySet()) {
                if (entry.getKey().getPartType() == WorkflowPartType.PART && entry.getValue() > 1) {
                    errorMessages.add("Für den WorkflowPart " + entry.getKey().getName() + " ist mehr als eine ausgehende Transition vorhanden. Das ist für den Workflowparttype part nicht erlaubt!");
                }
            }

            for (final WorkflowPart wp : allWorkflowParts) {
                if ((wp.getPartType() == WorkflowPartType.DECISION || wp.getPartType() == WorkflowPartType.PART) && !workflowPartToOutgoingTransitionCountMap.containsKey(wp)) {
                    errorMessages.add("Für den Workflowpart " + wp.getName() + " wurde keine ausgehende Transition deklariert. Bitte prüfen!");
                }

                if (!workflowPartsWithIngoeingTransitions.contains(wp) && !workflow.getWorkflowStart().getId().equals(wp.getId())) {
                    errorMessages.add("Für den Workflowpart " + wp.getName() + " wurde keine eingehende Transition deklariert. Bitte prüfen!");
                }
            }

            //set startpart for workflow
            if (workflow.getWorkflowStart() == null) {
                errorMessages.add("Es wurde kein Startelement für den Workflow gesetzt oder es ist keins angegeben.");
            }

            if (!hasEndPart) {
                errorMessages.add("Es existiert kein Endelement für den Workflow!");
            }

            if (!errorMessages.isEmpty()) {
                workflowRepository.delete(workflow);
                return new WorkflowImportResult(false, errorMessages);
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