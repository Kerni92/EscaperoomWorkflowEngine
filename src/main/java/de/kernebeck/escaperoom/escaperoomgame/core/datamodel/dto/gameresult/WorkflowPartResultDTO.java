package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult;

import java.io.Serializable;
import java.util.List;

public class WorkflowPartResultDTO implements Serializable {

    private Long id;

    private String name;

    private long elapsedTimeInMinutes;

    private List<RiddleResultDTO> riddleInformations;

    public WorkflowPartResultDTO(Long id, String name, long elapsedTimeInMinutes, List<RiddleResultDTO> riddleInformations) {
        this.id = id;
        this.name = name;
        this.elapsedTimeInMinutes = elapsedTimeInMinutes;
        this.riddleInformations = riddleInformations;
    }

    public Long getId() {
        return id;
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
