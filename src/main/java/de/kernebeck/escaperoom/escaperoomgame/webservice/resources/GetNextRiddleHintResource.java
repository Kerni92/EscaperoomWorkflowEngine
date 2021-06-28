package de.kernebeck.escaperoom.escaperoomgame.webservice.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetNextRiddleHintResource implements Serializable {

    private String gameId;

    private Long riddleId;

    public GetNextRiddleHintResource() {
        //default constructor required for jackson
    }

    public GetNextRiddleHintResource(String gameId, Long riddleId) {
        this.gameId = gameId;
        this.riddleId = riddleId;
    }

    public String getGameId() {
        return gameId;
    }

    public Long getRiddleId() {
        return riddleId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setRiddleId(Long riddleId) {
        this.riddleId = riddleId;
    }
}
