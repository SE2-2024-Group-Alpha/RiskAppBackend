package se2.alpha.riskappbackend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.model.game.GameSession;
import se2.alpha.riskappbackend.model.game.UserState;
import se2.alpha.riskappbackend.model.websocket.*;
import se2.alpha.riskappbackend.service.GameService;

import java.util.Objects;

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
            case USER_READY -> handleUserReady(session, message);
            case USER_LEAVE -> handleUserLeave(session, message);
        }
    }

//    TODO CHECK IF THIS WORKS!!!!
    private void handleUserLeave(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        UserLeaveWebsocketMessage userLeaveWebsocketMessage = gson.fromJson((String) message.getPayload(), UserLeaveWebsocketMessage.class);
        GameSession gameSession = gameService.leaveSession(userLeaveWebsocketMessage.getGameSessionId(), session);
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

    public void handleJoin(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JoinWebsocketMessage joinWebsocketMessage = gson.fromJson((String) message.getPayload(), JoinWebsocketMessage.class);
        GameSession gameSession = gameService.joinSession(joinWebsocketMessage.getGameSessionId(), session);
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

    public void handleUserReady(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        UserReadyWebsocketMessage userReadyWebsocketMessage = gson.fromJson((String) message.getPayload(), UserReadyWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(userReadyWebsocketMessage.getGameSessionId());
        String userName = Objects.requireNonNull(session.getPrincipal()).getName();
        UserState currentState = gameSession.getUserState(userName);
        currentState.setIsReady(userReadyWebsocketMessage.getIsReady());
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }
}
