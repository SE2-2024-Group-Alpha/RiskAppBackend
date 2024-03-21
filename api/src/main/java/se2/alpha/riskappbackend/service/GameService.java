package se2.alpha.riskappbackend.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.model.game.GameSession;

import java.util.*;


@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private Map<UUID, GameSession> gameSessions;

    @PostConstruct
    public void init() {
        gameSessions = new HashMap<>();
        var test = new GameSession("Test Game");
        gameSessions.put(test.getSessionId(), test);
    }

    public List<GameSession> getJoinableSessions() {
        return new ArrayList<GameSession>(gameSessions.values());
    }

    public UUID createNewSessions(String name) {
        GameSession newSession = new GameSession(name);
        gameSessions.put(newSession.getSessionId(), newSession);
        return newSession.getSessionId();
    }

    public void joinSessions(UUID sessionId, WebSocketSession userSession) {
        GameSession session = gameSessions.get(sessionId);
        session.join(userSession);
    }

    public void leaveSessions(UUID sessionId, WebSocketSession userSession) {
        GameSession session = gameSessions.get(sessionId);
        session.leave(userSession);
    }
}