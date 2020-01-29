package com.axway.client.socket;

import javax.annotation.Nonnull;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic WebSocket resource used to communicate with frontend.
 */
@ServerEndpoint("/ws")
public class WebSocketClient {

    /**
     * A static mapping of all associated connections from a UI instance.
     */
    private final static Set<Session> SESSIONS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Registers a new session with the internal mapping.
     *
     * @param session
     *      the browser session opening a new connection.
     */
    @OnOpen
    public void open(Session session) {
        SESSIONS.add(session);
    }

    /**
     * Removes a session from the internal mapping.
     *
     * @param session
     *      the browser session closing their connection.
     */
    @OnClose
    public void close(Session session, CloseReason cr) {
        SESSIONS.remove(session);
    }

    /**
     * Broadcasts a message to all connected sessions.
     *
     * @param message
     *      the message content to emit to the sessions.
     */
    public static void send(@Nonnull String message) {
        for (Session session : SESSIONS) {
            session.getAsyncRemote().sendText(message);
        }
    }
}
