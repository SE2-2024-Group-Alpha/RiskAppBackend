package se.alpha.riskappbackend.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.WebSocketSession;
import se.alpha.riskappbackend.model.game.GameSession;
import se.alpha.riskappbackend.service.GameService;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;


import java.security.Principal;

import java.util.List;
import java.util.UUID;

public class GameServiceTest {




    private GameService gameService;

    @Mock
    private WebSocketSession userSession;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameService = new GameService();
        when(userSession.getPrincipal()).thenReturn(principal);
    }

    @Test
    public void testInitialSession() {
        List<GameSession> sessions = gameService.getJoinableSessions();
        assertEquals(1, sessions.size());
        assertEquals("Test Game", sessions.get(0).getName());
    }

    @Test
    public void testCreateNewSession() {
        UUID sessionId = gameService.createNewSession("New Game");
        GameSession newSession = gameService.getGameSessionById(sessionId);

        assertNotNull(newSession);
        assertEquals("New Game", newSession.getName());
    }

    @Test
    public void testGetGameSessionById() {
        UUID sessionId = gameService.createNewSession("Specific Game");
        GameSession session = gameService.getGameSessionById(sessionId);

        assertNotNull(session);
        assertEquals("Specific Game", session.getName());
    }

    @Test
    public void testJoinSession() {
        // Arrange
        UUID sessionId = gameService.createNewSession("Test Session");
        String userName = "testUser";
        when(principal.getName()).thenReturn(userName);

        // Act
        GameSession session = gameService.joinSession(sessionId, userSession);

        // Assert
        assertNotNull(session);
        assertEquals(1, session.getUsers().intValue());
        assertTrue(session.getUserNames().contains(userName));
    }

    @Test
    public void testLeaveSession() {
        // Arrange
        UUID sessionId = gameService.createNewSession("Test Session");
        String userName = "testUser";
        when(principal.getName()).thenReturn(userName);
        gameService.joinSession(sessionId, userSession);

        // Act
        GameSession session = gameService.leaveSession(sessionId, userSession);

        // Assert
        assertNotNull(session);
        assertEquals(0, session.getUsers().intValue());
        assertFalse(session.getUserNames().contains(userName));
        assertNull(gameService.getGameSessionById(sessionId)); // Session should be removed if empty
    }
}