package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckRiddleSolutionResource {

    private String gameId;

    private Long riddleId;

    private String solution;

    public CheckRiddleSolutionResource() {
        //empty constructor required for Jackson
    }

    public CheckRiddleSolutionResource(String gameId, Long riddleId, String solution) {
        this.gameId = gameId;
        this.riddleId = riddleId;
        this.solution = solution;
    }

    public String getGameId() {
        return gameId;
    }

    public Long getRiddleId() {
        return riddleId;
    }

    public String getSolution() {
        return solution;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setRiddleId(Long riddleId) {
        this.riddleId = riddleId;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
