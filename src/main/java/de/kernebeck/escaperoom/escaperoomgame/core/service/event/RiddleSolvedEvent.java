package de.kernebeck.escaperoom.escaperoomgame.core.service.event;

import java.io.Serializable;
import java.util.Objects;

public class RiddleSolvedEvent implements Serializable {

    private static final long serialVersionUID = 673667055344485724L;

    private String gameId;

    private Long riddleId;

    public RiddleSolvedEvent(String gameId, Long riddleId) {
        this.gameId = gameId;
        this.riddleId = riddleId;
    }

    public String getGameId() {
        return gameId;
    }

    public Long getRiddleId() {
        return riddleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiddleSolvedEvent that = (RiddleSolvedEvent) o;
        return Objects.equals(gameId, that.gameId) && Objects.equals(riddleId, that.riddleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, riddleId);
    }
}
