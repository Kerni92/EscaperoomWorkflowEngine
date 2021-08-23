package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

import java.io.Serializable;
import java.util.Objects;

public class PauseGameEvent implements Serializable, IWebSocketPushMessage {

    private static final long serialVersionUID = 673667055344485724L;

    private String gameId;


    public PauseGameEvent(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PauseGameEvent that = (PauseGameEvent) o;
        return Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }
}

