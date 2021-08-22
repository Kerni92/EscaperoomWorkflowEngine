package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

import java.io.Serializable;
import java.util.Objects;

public class UpdateDialogEvent implements Serializable, IWebSocketPushMessage {

    private static final long serialVersionUID = 673667055344485724L;

    private String gameId;


    public UpdateDialogEvent(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateDialogEvent that = (UpdateDialogEvent) o;
        return Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }
}

