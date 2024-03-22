package se2.alpha.riskappbackend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.model.game.GameSession;
import se2.alpha.riskappbackend.model.websocket.*;
import se2.alpha.riskappbackend.service.GameService;

public class GameWebSocketHandler {

    private GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(RiskWebSocketHandler.class);
    private final Gson gson = new Gson();

    public GameWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    private void sendMessageToAll(GameSession gameSession, IGameWebsocketMessage message) {
        String messageContent = gson.toJson(message);
        TextMessage textMessage = new TextMessage(messageContent);

        for (WebSocketSession session : gameSession.getUserSessions().values()) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (Exception e) {
                logger.error("Error sending message to WebSocketSession: " + e.getMessage());
            }
        }
    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        IGameWebsocketMessage gameWebsocketMessage = gson.fromJson((String) message.getPayload(), GameWebsocketMessage.class);
        logger.info("Received Game Message with Action: " + gameWebsocketMessage.getAction());
        switch (gameWebsocketMessage.getAction()){
            case JOIN -> handleJoin(session, message);
        }
    }

    public void handleJoin(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JoinWebsocketMessage joinWebsocketMessage = gson.fromJson((String) message.getPayload(), JoinWebsocketMessage.class);
        GameSession gameSession = gameService.joinSessions(joinWebsocketMessage.getGameSessionId(), session);
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getUserNames());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

}
