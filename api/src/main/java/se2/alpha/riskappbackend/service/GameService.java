package se2.alpha.riskappbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.model.game.GameSession;

import java.util.*;


@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final Map<UUID, GameSession> gameSessions;

    public GameService() {
        this.gameSessions = new HashMap<>();
        var test = new GameSession("Test Game");
        this.gameSessions.put(test.getSessionId(), test);
    }

    public List<GameSession> getJoinableSessions() {
        return new ArrayList<GameSession>(gameSessions.values());
    }

    public UUID createNewSession(String name) {
        GameSession newSession = new GameSession(name);
        gameSessions.put(newSession.getSessionId(), newSession);
        return newSession.getSessionId();
    }

    public GameSession joinSession(UUID sessionId, WebSocketSession userSession) {
        GameSession session = gameSessions.get(sessionId);
        session.join(userSession);
        return session;
    }

    public GameSession leaveSession(UUID sessionId, WebSocketSession userSession) {
        GameSession session = gameSessions.get(sessionId);
        session.leave(userSession);
        if (session.isEmpty()){
            gameSessions.remove(sessionId);
        }

        return session;
    }

    public GameSession getGameSessionById(UUID sessionId){
        return gameSessions.get(sessionId);
    }
}