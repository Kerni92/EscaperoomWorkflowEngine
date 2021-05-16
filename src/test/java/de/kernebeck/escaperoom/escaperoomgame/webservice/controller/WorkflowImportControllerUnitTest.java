package de.kernebeck.escaperoom.escaperoomgame.webservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.SolutionType;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.enumeration.WorkflowPartType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class WorkflowImportControllerUnitTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void createNewWorkflow() throws Exception {

        //create start
        final WorkflowPartDTO start = new WorkflowPartDTO("Start", "Hier beginnt die Geschichte der Flucht aus dem Nimmerland.", WorkflowPartType.PART.getEnumerationValue(), "Start",
                Arrays.asList(new SolutionDTO("Startlösung", "In welche Richtung muss gegangen werden?", SolutionType.TEXT.getEnumerationValue(), "Osten", null)),
                Arrays.asList(new RiddleDTO("Wohin gehts?", 0, "In welche Himmelsrichtungs müssen wir gehen? Der Kreis spielt eine Rolle.", Arrays.asList(new RiddleHintDTO("1. Hinweis", "Welche " +
                        "Vier Himmelsrichtungen gibt es? Welche beinhaltet am Anfang einen Kreis?"), new RiddleHintDTO("Lösung", "Osten ist dir richtige Lösung. Nur die Himmelsrichtung beinhaltet das O am Anfang."))))
        );
        final WorkflowPartDTO zweiterAbschnitt = new WorkflowPartDTO("2ter Abschnitt", "Du bist in einer Wüste angekommen. In weiter Ferne eine Oase und auf dem Boden vor dir tauchen merkwürdige Karten auf. Was ist zu tun?", WorkflowPartType.PART.getEnumerationValue(),
                "2ter Abschnitt", Arrays.asList(new SolutionDTO("Lösung 1", "Wieviel Wasser muss in das Gefäß um den Durchgang zu öffnen?", SolutionType.TEXT.getEnumerationValue(), "2L", null),
                new SolutionDTO("Lösung2", "Welcher der Vier Steine muss verschoben werden?", SolutionType.MULTITEXT.getEnumerationValue(), "Löwenstein", Arrays.asList("Hasenstein", "Löwenstein", "Bierstein", "Baumstein"))),
                Arrays.asList(new RiddleDTO("Wo ist das Wasser?", 0, "Dort ist ein Krug der 5 Liter fasst, mit einem mehrwürdigen Zeichen drauf. Es erinnert die an ein bekannte Animeserie in der sich Kämpfer nach harten Training transformieren. " +
                        "Das Symbol erinnert an eine Zahl. Wieviel Liter Wasser müssen in den Krug?", Arrays.asList(new RiddleHintDTO("1. Hinweis", "In der Serie gibt es einen Feind, der heißt übersetzt Zelle.."),
                        new RiddleHintDTO("Lösung", "Zelle übersetzt heißt Cell. Kombiniert mit den ersten Hinweisen bleibt nur Dragonball Z als Serie. Man nehme das Z aus Dragenball und siehe eine 2. Die richtige Antwort lautet 2L"))))
        );

        final WorkflowPartDTO wahl = new WorkflowPartDTO("Wahlpart", "Du bist an einer Kreuzung. Der linke Weg scheint dunkler aber es ist ein Licht zu erkennen. Der rechte scheint kurz und einfach zu sein. Welchen wählst du?", WorkflowPartType.DECISION.getEnumerationValue(), "wahlpart", null, null);

        final WorkflowPartDTO shortWay = new WorkflowPartDTO("Links", "Da vorne ist ein Portal, es erwartet einen Zahlencode. Netterweise hat jemand eine Notiz hinterlassen.", WorkflowPartType.PART.getEnumerationValue(), "shortWay",
                Arrays.asList(new SolutionDTO("zahlencode", "", SolutionType.TEXT.getEnumerationValue(), "369", null)),
                Arrays.asList(new RiddleDTO("Zahlenrätsel", 0, "Du liest die Notiz. Wähle aufsteigend drei einzelne Ziffern die glatt durch drei teilbar sind.",
                        Arrays.asList(new RiddleHintDTO("Hinweie 1", "Lies den Text genau und überlege was berechnet werden muss. Hinweis: es wird von einstelligen Ziffern gesprochen!"),
                                new RiddleHintDTO("Lösung", "Dort steht drei Ziffern die glatt durch drei Teilbar sind. Welche sind das im Bereich 0-9? die Lösung lautet 369"))))
        );

        final WorkflowPartDTO longway = new WorkflowPartDTO("Rechts", "Noch mehr überlegen", WorkflowPartType.PART.getEnumerationValue(), "longWay",
                Arrays.asList(new SolutionDTO("zahlencode", "", SolutionType.TEXT.getEnumerationValue(), "369", null)),
                Arrays.asList(new RiddleDTO("Zahlenrätsel", 0, "Du liest die Notiz. Wähle aufsteigend drei einzelne Ziffern die glatt durch drei teilbar sind.",
                        Arrays.asList(new RiddleHintDTO("Hinweie 1", "Lies den Text genau und überlege was berechnet werden muss. Hinweis: es wird von einstelligen Ziffern gesprochen!"),
                                new RiddleHintDTO("Lösung", "Dort steht drei Ziffern die glatt durch drei Teilbar sind. Welche sind das im Bereich 0-9? die Lösung lautet 369"))))
        );

        final WorkflowPartDTO ende = new WorkflowPartDTO("Ende", "Du hast es geschafft zu fliehen. Herzlichen Glückwunsch!", WorkflowPartType.ENDPART.getEnumerationValue(), "ende", null, null);
        final List<WorkflowPartDTO> workflowPartDTOList = new ArrayList<>(Arrays.asList(start, zweiterAbschnitt, wahl, shortWay, longway, ende));
        final List<WorkflowTransitionDTO> transitionDTOS = new ArrayList<>(Arrays.asList(
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "Start", "2ter Abschnitt"),
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "2ter Abschnitt", "wahlpart"),
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "wahlpart", "shortWay"),
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "wahlpart", "longWay"),
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "shortWay", "ende"),
                new WorkflowTransitionDTO("starttosecond", "blub", "t1", "longWay", "ende")
        ));


        final WorkflowDTO workflowDTO = new WorkflowDTO("Flucht aus dem Nimmerland", "Start", workflowPartDTOList, transitionDTOS);

        final String result = new ObjectMapper().writeValueAsString(workflowDTO);

    }
}