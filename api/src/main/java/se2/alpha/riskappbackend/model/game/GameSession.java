package se2.alpha.riskappbackend.model.game;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSession {
    @Getter
    private UUID sessionId;
    private Map<String, WebSocketSession> userSessions;

    public GameSession() {
        this.sessionId = UUID.randomUUID();
        this.userSessions = new HashMap<>();
    }
}
