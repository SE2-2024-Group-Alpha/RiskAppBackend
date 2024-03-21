package se2.alpha.riskappbackend.websocket;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import com.google.gson.Gson;
import se2.alpha.riskappbackend.model.websocket.CustomWebsocketMessage;
import se2.alpha.riskappbackend.model.websocket.CustomWebsocketMessageType;
import se2.alpha.riskappbackend.model.websocket.ICustomWebsocketMessage;
import se2.alpha.riskappbackend.service.GameService;

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
        logger.info("Connection established with session id: " + session.getId());
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("Received Message: " + message.toString());
        ICustomWebsocketMessage msg = gson.fromJson((String) message.getPayload(), CustomWebsocketMessage.class);
        if(msg.getType() == CustomWebsocketMessageType.GAME) {
            gameWebSocketHandler.handleMessage(session, message);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
