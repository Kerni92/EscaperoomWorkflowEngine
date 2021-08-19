package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class GameResultDTO implements Serializable {

    private String name;

    private String gameId;

    private Timestamp startTime;

    private Timestamp endTime;

    private long elapsedTimeInMinutes;

    private List<WorkflowPartResultDTO> playedWorkflowParts;

    public GameResultDTO(String name, String gameId, Timestamp startTime, Timestamp endTime, long elapsedTimeInMinutes, List<WorkflowPartResultDTO> playedWorkflowParts) {
        this.name = name;
        this.gameId = gameId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.elapsedTimeInMinutes = elapsedTimeInMinutes;
        this.playedWorkflowParts = playedWorkflowParts;
    }

    public String getName() {
        return name;
    }

    public String getGameId() {
        return gameId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public long getElapsedTimeInMinutes() {
        return elapsedTimeInMinutes;
    }

    public List<WorkflowPartResultDTO> getPlayedWorkflowParts() {
        return playedWorkflowParts;
    }
}
