package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult;

import java.io.Serializable;
import java.util.List;

public class WorkflowPartResultDTO implements Serializable {

    private String name;

    private long elapsedTimeInMinutes;

    private List<RiddleResultDTO> riddleInformations;

    public WorkflowPartResultDTO(String name, long elapsedTimeInMinutes, List<RiddleResultDTO> riddleInformations) {
        this.name = name;
        this.elapsedTimeInMinutes = elapsedTimeInMinutes;
        this.riddleInformations = riddleInformations;
    }

    public String getName() {
        return name;
    }

    public long getElapsedTimeInMinutes() {
        return elapsedTimeInMinutes;
    }

    public List<RiddleResultDTO> getRiddleInformations() {
        return riddleInformations;
    }
}
