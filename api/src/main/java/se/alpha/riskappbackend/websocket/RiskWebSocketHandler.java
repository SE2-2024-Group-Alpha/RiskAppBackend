package se.alpha.riskappbackend.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import com.google.gson.Gson;

import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.model.websocket.CustomWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.CustomWebsocketMessageType;
import se.alpha.riskappbackend.model.websocket.ICustomWebsocketMessage;

@Component
public class RiskWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(RiskWebSocketHandler.class);
    private final Gson gson = new Gson();
    private final GameWebSocketHandler gameWebSocketHandler;

    public RiskWebSocketHandler(GameService gameService) {
        this.gameWebSocketHandler = new GameWebSocketHandler(gameService);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info(String.format("Connection established with session id: %1$s", session.getId()));
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info(String.format("Received Message: %1$s", message.toString()));
        ICustomWebsocketMessage msg = gson.fromJson((String) message.getPayload(), CustomWebsocketMessage.class);
        if(msg.getType() == CustomWebsocketMessageType.GAME) {
            gameWebSocketHandler.handleMessage(session, message);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        logger.info(String.format("Transport error: %1$s", exception.getMessage()));
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        logger.info(String.format("Connection closed: %1$s", session.getId()));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
