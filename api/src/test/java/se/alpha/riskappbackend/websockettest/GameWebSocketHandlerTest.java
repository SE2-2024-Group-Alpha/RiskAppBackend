package se.alpha.riskappbackend.websockettest;


import com.google.gson.Gson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import se.alpha.riskappbackend.model.db.Player;

import se.alpha.riskappbackend.model.game.GameSession;

import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.websocket.GameWebSocketHandler;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class GameWebSocketHandlerTest {

    @Mock
    private GameService gameService;

    @Mock
    private WebSocketSession mockSession;

    @InjectMocks
    private GameWebSocketHandler gameWebSocketHandler;



    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        gameWebSocketHandler = new GameWebSocketHandler(gameService);


    }



    @Test
    public void testHandleMessageWithLeaveAction() throws Exception {
        String jsonMessage = "{\"action\":\"USER_LEAVE\"}";
        WebSocketMessage<String> message = new TextMessage(jsonMessage);

        GameSession gameSession = mock(GameSession.class);
        when(gameService.leaveSession(any(), eq(mockSession))).thenReturn(gameSession);

        gameWebSocketHandler.handleMessage(mockSession, message);

        verify(gameService, times(1)).leaveSession(any(), eq(mockSession));
        verify(gameSession, times(1)).getReadyUsers();
    }



    @Test
    public void testHandleMessageWithEndTurnAction() throws Exception {
        String jsonMessage = "{\"action\":\"END_TURN\"}";
        WebSocketMessage<String> message = new TextMessage(jsonMessage);

        GameSession gameSession = mock(GameSession.class);
        Player player = mock(Player.class);
        when(gameService.getGameSessionById(any())).thenReturn(gameSession);
        when(gameSession.endTurn()).thenReturn(player);

        gameWebSocketHandler.handleMessage(mockSession, message);

        verify(gameService, times(1)).getGameSessionById(any());
        verify(gameSession, times(1)).endTurn();
    }





    @Test
    public void testHandleNullSession()  {
        String jsonMessage = "{\"action\":\"JOIN\"}";
        WebSocketMessage<String> message = new TextMessage(jsonMessage);

        Assertions.assertThrows(NullPointerException.class, () -> gameWebSocketHandler.handleMessage(null, message));
    }

    @Test
    public void testHandleNullMessage() {
        Assertions.assertThrows(NullPointerException.class, () -> gameWebSocketHandler.handleMessage(mockSession, null));
    }


}