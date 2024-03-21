package se2.alpha.riskappbackend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.model.websocket.CustomWebsocketMessage;
import se2.alpha.riskappbackend.model.websocket.GameWebsocketMessage;
import se2.alpha.riskappbackend.service.GameService;

@Component
public class GameWebSocketHandler {

    @Autowired
    private GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(RiskWebSocketHandler.class);
    private final Gson gson = new Gson();

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        GameWebsocketMessage gameWebsocketMessage = gson.fromJson((String) message.getPayload(), GameWebsocketMessage.class);
        logger.info("Received Game Message: " + gameWebsocketMessage.getAction());
    }

}
