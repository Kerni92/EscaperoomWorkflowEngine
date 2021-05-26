package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResource extends BasicResource {

    private String gameId;

    private Timestamp startTime;

    private Timestamp endTime;

    private Long totalTime;

    private List<String> usernames;

    public GameResource() {
        //empty Constructor for jackson
    }

    public GameResource(String gameId, Timestamp startTime, Timestamp endTime, Long totalTime, List<String> usernames) {
        super();
        this.gameId = gameId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.usernames = usernames;
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

    public Long getTotalTime() {
        return totalTime;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
