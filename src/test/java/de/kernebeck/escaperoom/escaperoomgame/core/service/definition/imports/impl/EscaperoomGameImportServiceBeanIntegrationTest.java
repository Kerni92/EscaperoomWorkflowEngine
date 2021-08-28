package de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractIntegrationTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleHintRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.SolutionRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class EscaperoomGameImportServiceBeanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private EscaperoomGameImportService escaperoomGameImportService;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private RiddleHintRepository riddleHintRepository;

    @Test
    public void testImportSuccess() throws Exception {
        File importFile = null;
        try {
            importFile = File.createTempFile("import", "_import.json");
            final InputStreamReader inputStream = new InputStreamReader(EscaperoomGameImportServiceBeanIntegrationTest.class.getResourceAsStream("/importtest/game_success.json"), StandardCharsets.UTF_8);
            final FileWriter output = new FileWriter(importFile, StandardCharsets.UTF_8);
            IOUtils.copy(inputStream, output);
            inputStream.close();
            output.close();


            final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomgameFromFile(importFile);
            assertThat(result.isSuccess()).isTrue();
            verifyWorkflowInDatabase();
        }
        finally {
            if (importFile != null) {
                importFile.delete();
            }
        }
    }

    @Test
    public void testImportFailure() throws Exception {
        File importFile = null;
        try {
            importFile = File.createTempFile("import", "_import.json");
            final InputStreamReader inputStream = new InputStreamReader(EscaperoomGameImportServiceBeanIntegrationTest.class.getResourceAsStream("/importtest/game_failure.json"), StandardCharsets.UTF_8);
            final FileWriter output = new FileWriter(importFile, StandardCharsets.UTF_8);
            IOUtils.copy(inputStream, output);
            inputStream.close();
            output.close();


            final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomgameFromFile(importFile);
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorMessages()).isNotEmpty();
        }
        finally {
            if (importFile != null) {
                importFile.delete();
            }
        }
    }

    @AfterEach
    public void tearDown() {
        workflowRepository.deleteAll();
        workflowRepository.flush();
    }

    private void verifyWorkflowInDatabase() {
        //verify that workflow is correctly in database
        final Workflow workflow = workflowRepository.findAll().get(0);
        assertThat(workflow.getName()).isEqualTo("Flucht aus dem Nimmerland");

        final WorkflowPart startWP = workflow.getWorkflowStart();
        final WorkflowPart secondWorkflowPart = verifyStartWorkflowPart(startWP);
        final WorkflowPart thirdWorkflowPart = verifySecondWorkflowPart(secondWorkflowPart);
        final List<WorkflowPart> decisionOptions = verifyDecisionPart(thirdWorkflowPart);
        verifyLeftPart(decisionOptions.get(0));
        final WorkflowPart ende = verifyRightPart(decisionOptions.get(1));
        verifyEnde(ende);
    }

    private WorkflowPart verifyStartWorkflowPart(WorkflowPart startWP) {
        //verify startPart
        assertThat(startWP.getName()).isEqualTo("Start");
        assertThat(startWP.getDescription()).isEqualTo("Du wurdest in einer Wüste ausgesetzt. Wie finden wir einige Hinweistafeln die uns doch weiterhelfen müssen..");
        assertThat(startWP.getPartType()).isEqualTo(WorkflowPartType.PART);
        final List<Riddle> riddleList = startWP.getRiddles();
        riddleList.sort(Comparator.comparingInt(Riddle::getSortIndex));

        //assert riddle 1
        assertThat(riddleList.get(0).getName()).isEqualTo("Wohin gehts?");
        assertThat(riddleList.get(0).getContent()).isEqualTo("In welche Himmelsrichtungs müssen wir gehen? Der Kreis spielt eine Rolle. Welche Richtung lässt einen Kreis beginnen?");
        final List<RiddleHint> hintsRiddle1 = riddleHintRepository.findAllRiddleHintByRiddle(riddleList.get(0).getId());
        hintsRiddle1.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        assertThat(hintsRiddle1.get(0).getName()).isEqualTo("1. Hinweis");
        assertThat(hintsRiddle1.get(0).getContent()).isEqualTo("Welche Vier Himmelsrichtungen gibt es? Welche beinhaltet am Anfang einen Kreis?");
        assertThat(hintsRiddle1.get(1).getName()).isEqualTo("Lösung");
        assertThat(hintsRiddle1.get(1).getContent()).isEqualTo("Osten ist dir richtige Lösung. Nur die Himmelsrichtung beinhaltet das O am Anfang.");
        final List<Solution> solutions1 = solutionRepository.findByRiddle(riddleList.get(0));
        assertThat(solutions1.get(0).getName()).isEqualTo("Lösung 1");
        assertThat(solutions1.get(0).getDescription()).isEqualTo("In welche Richtung muss gegangen werden?");
        assertThat(solutions1.get(0).getType()).isEqualTo(SolutionType.TEXT);
        assertThat(solutions1.get(0).getSolution()).isEqualTo("Osten");
        assertThat(solutions1.get(0).getSolutionOptions()).isNull();


        //assert riddle 2
        assertThat(riddleList.get(1).getName()).isEqualTo("Wo steht die Sonne?");
        assertThat(riddleList.get(1).getContent()).isEqualTo("Es gibt eine Eselsbrücke welche den Verlauf der Sonne beschreibt. Auf welcher Seite zeigt Sie sich niemals?");
        final List<RiddleHint> hintsRiddle2 = riddleHintRepository.findAllRiddleHintByRiddle(riddleList.get(1).getId());
        hintsRiddle2.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        assertThat(hintsRiddle2.get(0).getName()).isEqualTo("1. Hinweis");
        assertThat(hintsRiddle2.get(0).getContent()).isEqualTo("Erinnere dich an die Eselsbrücke.. Wo versteckt sich die Sonne immer?");
        assertThat(hintsRiddle2.get(1).getName()).isEqualTo("Lösung");
        assertThat(hintsRiddle2.get(1).getContent()).isEqualTo("Die Eselsbrücke: Im Osten geht die Sonne auf, im Süden ist ihr Mittagslauf, im Westen will sie untergehn, im Norden ist sie nie zu sehn. Die Lösung sollte nun klar sein.");
        final List<Solution> solutions2 = solutionRepository.findByRiddle(riddleList.get(1));
        assertThat(solutions2.get(0).getName()).isEqualTo("Startlösung");
        assertThat(solutions2.get(0).getDescription()).isEqualTo("Wo geht die Sonne unter?");
        assertThat(solutions2.get(0).getType()).isEqualTo(SolutionType.TEXT);
        assertThat(solutions2.get(0).getSolution()).isEqualTo("Norden");
        assertThat(solutions2.get(0).getSolutionOptions()).isNull();

        //assert outgoing transitions
        assertThat(startWP.getOutgoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t = startWP.getOutgoingTransitions().iterator().next();
        assertThat(t.getName()).isEqualTo("Laufe Richtung Osten");
        assertThat(t.getDescription()).isEqualTo("blub");

        //no ingoin allowed
        assertThat(startWP.getIngoingTransitions().size()).isEqualTo(0);

        return t.getDestinationPart();
    }

    private WorkflowPart verifySecondWorkflowPart(WorkflowPart part) {
        //verify secondPart
        assertThat(part.getName()).isEqualTo("2ter Abschnitt");
        assertThat(part.getDescription()).isEqualTo("Du bist in einer Wüste angekommen. In weiter Ferne eine Oase und auf dem Boden vor dir tauchen merkwürdige Karten auf. Was ist zu tun?");
        assertThat(part.getPartType()).isEqualTo(WorkflowPartType.PART);
        final List<Riddle> riddleList = part.getRiddles();
        riddleList.sort(Comparator.comparingInt(Riddle::getSortIndex));

        //assert riddle 1
        assertThat(riddleList.get(0).getName()).isEqualTo("Wo ist das Wasser?");
        assertThat(riddleList.get(0).getContent()).isEqualTo("Dort ist ein Krug der 5 Liter fasst, mit einem mehrwürdigen Zeichen drauf. Es erinnert die an ein bekannte Animeserie in der sich Kämpfer nach harten Training transformieren. Das Symbol erinnert an eine Zahl. Wieviel Liter Wasser müssen in den Krug?");
        final List<RiddleHint> hintsRiddle1 = riddleHintRepository.findAllRiddleHintByRiddle(riddleList.get(0).getId());
        hintsRiddle1.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        assertThat(hintsRiddle1.get(0).getName()).isEqualTo("1. Hinweis");
        assertThat(hintsRiddle1.get(0).getContent()).isEqualTo("In der Serie gibt es einen Feind, der heißt übersetzt Zelle..");
        assertThat(hintsRiddle1.get(1).getName()).isEqualTo("Lösung");
        assertThat(hintsRiddle1.get(1).getContent()).isEqualTo("Zelle übersetzt heißt Cell. Kombiniert mit den ersten Hinweisen bleibt nur Dragonball Z als Serie. Man nehme das Z aus Dragenball und siehe eine 2. Die richtige Antwort lautet 2L");
        final List<Solution> solutions1 = solutionRepository.findByRiddle(riddleList.get(0));
        assertThat(solutions1.get(0).getName()).isEqualTo("Lösung 1");
        assertThat(solutions1.get(0).getDescription()).isEqualTo("Wieviel Wasser muss in das Gefäß um den Durchgang zu öffnen?");
        assertThat(solutions1.get(0).getType()).isEqualTo(SolutionType.TEXT);
        assertThat(solutions1.get(0).getSolution()).isEqualTo("2L");
        assertThat(solutions1.get(0).getSolutionOptions()).isNull();

        //assert outgoing transitions
        assertThat(part.getOutgoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t = part.getOutgoingTransitions().iterator().next();
        assertThat(t.getName()).isEqualTo("starttosecond");
        assertThat(t.getDescription()).isEqualTo("blub");
        assertThat(t.getDestinationPart().getName()).isEqualTo("Wahlpart");

        //assert ingoing transition
        assertThat(part.getIngoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t1 = part.getIngoingTransitions().iterator().next();
        assertThat(t1.getName()).isEqualTo("Laufe Richtung Osten");
        assertThat(t1.getDescription()).isEqualTo("blub");
        assertThat(t1.getSourcePart().getName()).isEqualTo("Start");

        return t.getDestinationPart();
    }

    private List<WorkflowPart> verifyDecisionPart(WorkflowPart part) {
        //verify startPart
        assertThat(part.getName()).isEqualTo("Wahlpart");
        assertThat(part.getDescription()).isEqualTo("Du bist an einer Kreuzung. Der linke Weg scheint dunkler aber es ist ein Licht zu erkennen. Der rechte scheint kurz und einfach zu sein. Welchen wählst du?");
        assertThat(part.getPartType()).isEqualTo(WorkflowPartType.DECISION);

        //assert outgoing transitions
        assertThat(part.getOutgoingTransitions().size()).isEqualTo(2);
        final List<WorkflowTransition> outgoingTransitions = new ArrayList<>(part.getOutgoingTransitions());
        outgoingTransitions.sort(Comparator.comparingInt(WorkflowTransition::getSortIndex));

        assertThat(outgoingTransitions.get(0).getName()).isEqualTo("starttosecond");
        assertThat(outgoingTransitions.get(0).getDescription()).isEqualTo("blub");
        assertThat(outgoingTransitions.get(0).getDestinationPart().getName()).isEqualTo("Links");

        assertThat(outgoingTransitions.get(1).getName()).isEqualTo("starttosecond");
        assertThat(outgoingTransitions.get(1).getDescription()).isEqualTo("blub");
        assertThat(outgoingTransitions.get(1).getDestinationPart().getName()).isEqualTo("Rechts");

        //assert ingoing transition
        assertThat(part.getIngoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t = part.getIngoingTransitions().iterator().next();
        assertThat(t.getName()).isEqualTo("starttosecond");
        assertThat(t.getDescription()).isEqualTo("blub");
        assertThat(t.getSourcePart().getName()).isEqualTo("2ter Abschnitt");

        return outgoingTransitions.stream().map(WorkflowTransition::getDestinationPart).collect(Collectors.toList());
    }

    private void verifyLeftPart(WorkflowPart part) {
        //verify secondPart
        assertThat(part.getName()).isEqualTo("Links");
        assertThat(part.getDescription()).isEqualTo("Da vorne ist ein Portal, es erwartet einen Zahlencode. Netterweise hat jemand eine Notiz hinterlassen.");
        assertThat(part.getPartType()).isEqualTo(WorkflowPartType.PART);
        final List<Riddle> riddleList = part.getRiddles();
        riddleList.sort(Comparator.comparingInt(Riddle::getSortIndex));

        //assert riddle 1
        assertThat(riddleList.get(0).getName()).isEqualTo("Zahlenrätsel");
        assertThat(riddleList.get(0).getContent()).isEqualTo("Du liest die Notiz. Wähle aufsteigend drei einzelne Ziffern die glatt durch drei teilbar sind.");
        final List<RiddleHint> hintsRiddle1 = riddleHintRepository.findAllRiddleHintByRiddle(riddleList.get(0).getId());
        hintsRiddle1.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        assertThat(hintsRiddle1.get(0).getName()).isEqualTo("Hinweie 1");
        assertThat(hintsRiddle1.get(0).getContent()).isEqualTo("Lies den Text genau und überlege was berechnet werden muss. Hinweis: es wird von einstelligen Ziffern gesprochen!");
        assertThat(hintsRiddle1.get(1).getName()).isEqualTo("Lösung");
        assertThat(hintsRiddle1.get(1).getContent()).isEqualTo("Dort steht drei Ziffern die glatt durch drei Teilbar sind. Welche sind das im Bereich 0-9? die Lösung lautet 369");
        final List<Solution> solutions1 = solutionRepository.findByRiddle(riddleList.get(0));
        assertThat(solutions1.get(0).getName()).isEqualTo("zahlencode");
        assertThat(solutions1.get(0).getDescription()).isEqualTo("");
        assertThat(solutions1.get(0).getType()).isEqualTo(SolutionType.TEXT);
        assertThat(solutions1.get(0).getSolution()).isEqualTo("369");
        assertThat(solutions1.get(0).getSolutionOptions()).isNull();

        //assert outgoing transitions
        assertThat(part.getOutgoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t = part.getOutgoingTransitions().iterator().next();
        assertThat(t.getName()).isEqualTo("starttosecond");
        assertThat(t.getDescription()).isEqualTo("blub");
        assertThat(t.getDestinationPart().getName()).isEqualTo("Ende");

        //assert ingoing transition
        assertThat(part.getIngoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t1 = part.getIngoingTransitions().iterator().next();
        assertThat(t1.getName()).isEqualTo("starttosecond");
        assertThat(t1.getDescription()).isEqualTo("blub");
        assertThat(t1.getSourcePart().getName()).isEqualTo("Wahlpart");
    }

    private WorkflowPart verifyRightPart(WorkflowPart part) {
        //verify secondPart
        assertThat(part.getName()).isEqualTo("Rechts");
        assertThat(part.getDescription()).isEqualTo("Noch mehr überlegen");
        assertThat(part.getPartType()).isEqualTo(WorkflowPartType.PART);
        final List<Riddle> riddleList = part.getRiddles();
        riddleList.sort(Comparator.comparingInt(Riddle::getSortIndex));

        //assert riddle 1
        assertThat(riddleList.get(0).getName()).isEqualTo("Zahlenrätsel");
        assertThat(riddleList.get(0).getContent()).isEqualTo("Du liest die Notiz. Wähle aufsteigend drei einzelne Ziffern die glatt durch drei teilbar sind.");
        final List<RiddleHint> hintsRiddle1 = riddleHintRepository.findAllRiddleHintByRiddle(riddleList.get(0).getId());
        hintsRiddle1.sort(Comparator.comparingInt(RiddleHint::getSortIndex));
        assertThat(hintsRiddle1.get(0).getName()).isEqualTo("Hinweie 1");
        assertThat(hintsRiddle1.get(0).getContent()).isEqualTo("Lies den Text genau und überlege was berechnet werden muss. Hinweis: es wird von einstelligen Ziffern gesprochen!");
        assertThat(hintsRiddle1.get(1).getName()).isEqualTo("Lösung");
        assertThat(hintsRiddle1.get(1).getContent()).isEqualTo("Dort steht drei Ziffern die glatt durch drei Teilbar sind. Welche sind das im Bereich 0-9? die Lösung lautet 369");
        final List<Solution> solutions1 = solutionRepository.findByRiddle(riddleList.get(0));
        assertThat(solutions1.get(0).getName()).isEqualTo("zahlencode");
        assertThat(solutions1.get(0).getDescription()).isEqualTo("");
        assertThat(solutions1.get(0).getType()).isEqualTo(SolutionType.TEXT);
        assertThat(solutions1.get(0).getSolution()).isEqualTo("369");
        assertThat(solutions1.get(0).getSolutionOptions()).isNull();

        //assert outgoing transitions
        assertThat(part.getOutgoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t = part.getOutgoingTransitions().iterator().next();
        assertThat(t.getName()).isEqualTo("starttosecond");
        assertThat(t.getDescription()).isEqualTo("blub");
        assertThat(t.getDestinationPart().getName()).isEqualTo("Ende");

        //assert ingoing transition
        assertThat(part.getIngoingTransitions().size()).isEqualTo(1);
        final WorkflowTransition t1 = part.getIngoingTransitions().iterator().next();
        assertThat(t1.getName()).isEqualTo("starttosecond");
        assertThat(t1.getDescription()).isEqualTo("blub");
        assertThat(t1.getSourcePart().getName()).isEqualTo("Wahlpart");

        return t.getDestinationPart();
    }

    private void verifyEnde(WorkflowPart part) {
        assertThat(part.getName()).isEqualTo("Ende");
        assertThat(part.getDescription()).isEqualTo("Du hast es geschafft zu fliehen. Herzlichen Glückwunsch!");
        assertThat(part.getPartType()).isEqualTo(WorkflowPartType.ENDPART);

        //assert outgoing transitions
        assertThat(part.getOutgoingTransitions().size()).isEqualTo(0);

        //assert ingoing transition
        assertThat(part.getIngoingTransitions().size()).isEqualTo(2);
        final List<WorkflowTransition> ingoingTransition = new ArrayList<>(part.getIngoingTransitions());
        ingoingTransition.sort(Comparator.comparingInt(WorkflowTransition::getSortIndex));
        final WorkflowTransition t1 = ingoingTransition.get(0);
        assertThat(t1.getName()).isEqualTo("starttosecond");
        assertThat(t1.getDescription()).isEqualTo("blub");
        assertThat(t1.getSourcePart().getName()).isEqualTo("Links");

        final WorkflowTransition t2 = ingoingTransition.get(1);
        assertThat(t2.getName()).isEqualTo("starttosecond");
        assertThat(t2.getDescription()).isEqualTo("blub");
        assertThat(t2.getSourcePart().getName()).isEqualTo("Rechts");
    }
}