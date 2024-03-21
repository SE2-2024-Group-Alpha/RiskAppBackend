package se2.alpha.riskappbackend.model.game;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSession {
    @Getter
    private UUID sessionId;
    @Getter
    private String name;
    @Getter
    private Integer users;
    @Getter
    private GameState state;
    private final Map<String, WebSocketSession> userSessions;

    public GameSession(String name) {
        this.name = name;
        this.sessionId = UUID.randomUUID();
        this.userSessions = new HashMap<>();
        this.state = GameState.Lobby;
        users = 0;
    }

    public GameSession(String name, WebSocketSession userSession) {
        this.name = name;
        this.sessionId = UUID.randomUUID();
        this.userSessions = new HashMap<>();
        this.userSessions.put(userSession.getId(), userSession);
        this.state = GameState.Lobby;
        users = 1;
    }

    public void join(WebSocketSession userSession) {
        userSessions.put(userSession.getId(), userSession);
        users++;
    }

    public void leave(WebSocketSession userSession) {
        userSessions.remove(userSession.getId());
        users--;
    }
}
