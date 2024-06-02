package se.alpha.riskappbackend;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

import se.alpha.riskappbackend.model.websocket.CustomWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.CustomWebsocketMessageType;
import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.websocket.GameWebSocketHandler;
import se.alpha.riskappbackend.websocket.RiskWebSocketHandler;

import java.lang.reflect.Field;


@ExtendWith(MockitoExtension.class)
public class RiskWebSocketHandlerTest {

    @Mock
    private WebSocketSession mockSession;
    @Mock
    private Gson mockGson;
    @Mock
    private GameService mockGameService;
    @Mock
    private GameWebSocketHandler mockGameWebSocketHandler;
    @InjectMocks
    private RiskWebSocketHandler riskWebSocketHandler;

    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        riskWebSocketHandler = new RiskWebSocketHandler(mockGameService);
    }

    @Test
    public void testAfterConnectionEstablished() throws Exception {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        when(mockSession.getId()).thenReturn("123");
        riskWebSocketHandler.afterConnectionEstablished(mockSession);
        verify(mockSession, times(1)).getId();
    }

    @Test
    public void testAfterConnectionClosed() throws Exception {
        riskWebSocketHandler.afterConnectionClosed(mockSession, CloseStatus.NORMAL);
        verify(mockSession, times(1)).getId();
    }





    @Test
    public void testHandleTransportError() {
        Exception exception = new RuntimeException("Network Error");

        riskWebSocketHandler.handleTransportError(mockSession, exception);


    }
    @Test
    public void testHandleMessageWithGameType() throws Exception {
        String jsonMessage = "{\"type\":\"GAME\"}";
        WebSocketMessage<String> message = new TextMessage(jsonMessage);

        CustomWebsocketMessage customMessage = new CustomWebsocketMessage();
        customMessage.setType(CustomWebsocketMessageType.GAME);

        when(mockGson.fromJson(anyString(), eq(CustomWebsocketMessage.class))).thenReturn(customMessage);


        Field gsonField = RiskWebSocketHandler.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        gsonField.set(riskWebSocketHandler, mockGson);
        Field gameHandlerField = RiskWebSocketHandler.class.getDeclaredField("gameWebSocketHandler");
        gameHandlerField.setAccessible(true);
        gameHandlerField.set(riskWebSocketHandler, mockGameWebSocketHandler);

        riskWebSocketHandler.handleMessage(mockSession, message);

        verify(mockGameWebSocketHandler).handleMessage(eq(mockSession), eq(message));
    }



}
