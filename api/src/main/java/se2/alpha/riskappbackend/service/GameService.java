package se2.alpha.riskappbackend.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se2.alpha.riskappbackend.model.game.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private List<GameSession> gameSessions = new ArrayList<>();

    @PostConstruct
    public void init() {
        gameSessions = new ArrayList<>();
    }

    public List<GameSession> getJoinableSessions() {
        return gameSessions;
    }

    public UUID createNewSessions() {
        GameSession newSession = new GameSession();
        gameSessions.add(newSession);
        return newSession.getSessionId();
    }
}