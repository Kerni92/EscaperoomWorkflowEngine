package de.kernebeck.escaperoom.escaperoomgame.webapp.service;

import com.google.common.eventbus.Subscribe;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.event.RiddleSolvedEvent;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.registry.SimpleWebSocketConnectionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketEventService {

    private static final WebSocketEventService INSTANCE = new WebSocketEventService();

    private final Map<String, List<ConnectedMessage>> connections = new ConcurrentHashMap<>();

    private final IWebSocketConnectionRegistry connectionRegistry = new SimpleWebSocketConnectionRegistry();

    private WebSocketEventService() {
        super();
    }

    public void registerConnection(ConnectedMessage message, String gameId) {
        connections.putIfAbsent(gameId, new ArrayList<>());
        connections.get(gameId).add(message);
    }

    public void unregisterConnection(ClosedMessage closedMessage, String gameId) {
        if (connections.get(gameId) != null) {
            for (int i = 0; i < connections.get(gameId).size(); i++) {
                ConnectedMessage m = connections.get(gameId).get(i);
                if (m.getSessionId().equals(closedMessage.getSessionId()) && m.getKey().equals(closedMessage.getKey())) {
                    connections.get(gameId).remove(i);
                }
            }
        }
    }

    @Subscribe
    public void handleRiddleSolvedEvent(RiddleSolvedEvent event) {
        sendEvent(event.getGameId(), event);
    }

    private void sendEvent(String gameId, IWebSocketPushMessage message) {
        if (connections.containsKey(gameId)) {
            Application a = Application.get();
            for (final ConnectedMessage m : connections.get(gameId)) {
                IWebSocketConnection connection = connectionRegistry.getConnection(a, m.getSessionId(), m.getKey());
                connection.sendMessage(message);
            }
        }
    }


    public static WebSocketEventService getInstance() {
        return INSTANCE;
    }
}
