package se.alpha.riskappbackend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import se.alpha.riskappbackend.model.websocket.AttackWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.CountryChangedWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.CreateGameWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.EndTurnWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.GameStartedWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.GameWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.IGameWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.JoinWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.MoveTroopsWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.NewTurnWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.PlayerEliminatedWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.PlayerWonWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.SeizeCountryWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.StrengthenCountryWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.UserLeaveWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.UserReadyWebsocketMessage;
import se.alpha.riskappbackend.model.websocket.UserSyncWebsocketMessage;
import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.model.db.Country;
import se.alpha.riskappbackend.model.db.Player;
import se.alpha.riskappbackend.model.game.GameSession;
import se.alpha.riskappbackend.model.game.UserState;

import java.util.ArrayList;
import java.util.Objects;

public class GameWebSocketHandler {

    private GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);
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
            case CREATE_GAME -> handleCreateGame(session, message);
            case END_TURN -> handleEndTurn(session, message);
            case SEIZE_COUNTRY -> handleSeizeCountry(session, message);
            case STRENGTHEN_COUNTRY -> handleStrengthenCountry(session, message);
            case MOVE_TROOPS -> handleMoveTroops(session, message);
            case ATTACK -> handleAttack(session, message);
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

    public void handleCreateGame(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        CreateGameWebsocketMessage createGameWebsocketMessage = gson.fromJson((String) message.getPayload(), CreateGameWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(createGameWebsocketMessage.getGameSessionId());
        gameSession.createGame(createGameWebsocketMessage.getPlayers());
        GameStartedWebsocketMessage gameStartedWebsocketMessage = new GameStartedWebsocketMessage(new ArrayList<>(gameSession.getPlayers()), gameSession.getActivePlayer());
        sendMessageToAll(gameSession, gameStartedWebsocketMessage);
    }

    public void handleEndTurn(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        EndTurnWebsocketMessage endTurnWebsocketMessage = gson.fromJson((String) message.getPayload(), EndTurnWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(endTurnWebsocketMessage.getGameSessionId());
        Player activePlayer = gameSession.endTurn();
        gameSession.getNewTroops(activePlayer.getId());
        NewTurnWebsocketMessage newTurnWebsocketMessage = new NewTurnWebsocketMessage(activePlayer);
        sendMessageToAll(gameSession, newTurnWebsocketMessage);
    }

    public void handleSeizeCountry(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        SeizeCountryWebsocketMessage seizeCountryWebsocketMessage = gson.fromJson((String) message.getPayload(), SeizeCountryWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(seizeCountryWebsocketMessage.getGameSessionId());
        gameSession.seizeCountry(seizeCountryWebsocketMessage.getPlayerId(), seizeCountryWebsocketMessage.getCountryName(), seizeCountryWebsocketMessage.getNumberOfTroops());
        CountryChangedWebsocketMessage countryChangedWebsocketMessage = new CountryChangedWebsocketMessage(seizeCountryWebsocketMessage.getPlayerId(), seizeCountryWebsocketMessage.getCountryName(), seizeCountryWebsocketMessage.getNumberOfTroops());
        sendMessageToAll(gameSession, countryChangedWebsocketMessage);
        Player activePlayer = gameSession.endTurn();
        NewTurnWebsocketMessage newTurnWebsocketMessage = new NewTurnWebsocketMessage(activePlayer);
        sendMessageToAll(gameSession, newTurnWebsocketMessage);
    }

    public void handleStrengthenCountry(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        StrengthenCountryWebsocketMessage strengthenCountryWebsocketMessage = gson.fromJson((String) message.getPayload(), StrengthenCountryWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(strengthenCountryWebsocketMessage.getGameSessionId());
        int numberOfTroops = gameSession.strengthenCountry(strengthenCountryWebsocketMessage.getPlayerId(), strengthenCountryWebsocketMessage.getCountryName(), strengthenCountryWebsocketMessage.getNumberOfTroops());
        CountryChangedWebsocketMessage countryChangedWebsocketMessage = new CountryChangedWebsocketMessage(strengthenCountryWebsocketMessage.getPlayerId(), strengthenCountryWebsocketMessage.getCountryName(), numberOfTroops);
        sendMessageToAll(gameSession, countryChangedWebsocketMessage);
    }

    public void handleMoveTroops(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        MoveTroopsWebsocketMessage moveTroopsWebsocketMessage = gson.fromJson((String) message.getPayload(), MoveTroopsWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(moveTroopsWebsocketMessage.getGameSessionId());
        gameSession.moveTroops(moveTroopsWebsocketMessage.getPlayerId(), moveTroopsWebsocketMessage.getMoveFromCountryName(), moveTroopsWebsocketMessage.getMoveToCountryName(), moveTroopsWebsocketMessage.getNumberOfTroops());
        Country moveFromCountry = gameSession.getCountryByName(moveTroopsWebsocketMessage.getMoveFromCountryName());
        CountryChangedWebsocketMessage moveFromCountryChangedWebsocketMessage = new CountryChangedWebsocketMessage(moveFromCountry.getOwner().getId(), moveFromCountry.getName(), moveFromCountry.getNumberOfTroops());
        sendMessageToAll(gameSession, moveFromCountryChangedWebsocketMessage);
        Country moveToCountry = gameSession.getCountryByName(moveTroopsWebsocketMessage.getMoveToCountryName());
        CountryChangedWebsocketMessage moveToCountryChangedWebsocketMessage = new CountryChangedWebsocketMessage(moveToCountry.getOwner().getId(), moveToCountry.getName(), moveToCountry.getNumberOfTroops());
        sendMessageToAll(gameSession, moveToCountryChangedWebsocketMessage);
    }

    public void handleAttack(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        AttackWebsocketMessage attackWebsocketMessage = gson.fromJson((String) message.getPayload(), AttackWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(attackWebsocketMessage.getGameSessionId());
        gameSession.attack(attackWebsocketMessage.getAttackerPlayerId(), attackWebsocketMessage.getDefenderPlayerId(), attackWebsocketMessage.getAttackingCountryName(), attackWebsocketMessage.getDefendingCountryName());
        Country attackingCountry = gameSession.getCountryByName(attackWebsocketMessage.getAttackingCountryName());
        CountryChangedWebsocketMessage moveFromCountryChangedWebsocketMessage = new CountryChangedWebsocketMessage(attackingCountry.getOwner().getId(), attackingCountry.getName(), attackingCountry.getNumberOfTroops());
        sendMessageToAll(gameSession, moveFromCountryChangedWebsocketMessage);
        Country defendingCountry = gameSession.getCountryByName(attackWebsocketMessage.getDefendingCountryName());
        CountryChangedWebsocketMessage moveToCountryChangedWebsocketMessage = new CountryChangedWebsocketMessage(defendingCountry.getOwner().getId(), defendingCountry.getName(), defendingCountry.getNumberOfTroops());
        sendMessageToAll(gameSession, moveToCountryChangedWebsocketMessage);

        if(gameSession.isPlayerEliminated(attackWebsocketMessage.getDefenderPlayerId())) {
            if(gameSession.hasPlayerWon(attackWebsocketMessage.getAttackerPlayerId()))
            {
                PlayerWonWebsocketMessage playerWonWebsocketMessage = new PlayerWonWebsocketMessage(attackWebsocketMessage.getAttackerPlayerId());
            }
            else
            {
                PlayerEliminatedWebsocketMessage playerEliminatedWebsocketMessage = new PlayerEliminatedWebsocketMessage(attackWebsocketMessage.getDefenderPlayerId());
            }
        }
    }
}
