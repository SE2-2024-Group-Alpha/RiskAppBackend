package se.alpha.riskappbackend.websocket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import se.alpha.riskappbackend.model.exception.RiskException;
import se.alpha.riskappbackend.model.websocket.*;
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
    private static final Gson gson = new Gson();

    public GameWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    private static void sendMessageToAll(GameSession gameSession, IGameWebsocketMessage message) {
        String messageContent = gson.toJson(message);
        TextMessage textMessage = new TextMessage(messageContent);

        for (WebSocketSession session : gameSession.getUserSessions().values()) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (Exception e) {
                logger.error(String.format("Error sending message to WebSocketSession: %1$s", e.getMessage()));
            }
        }
    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        IGameWebsocketMessage gameWebsocketMessage = gson.fromJson((String) message.getPayload(), GameWebsocketMessage.class);
        logger.info(String.format("Received Game Message with Action: %1$s", gameWebsocketMessage.getAction()));
        switch (gameWebsocketMessage.getAction()){
            case JOIN -> handleJoin(session, message);
            case USER_READY -> handleUserReady(session, message);
            case USER_LEAVE -> handleUserLeave(session, message);
            case CREATE_GAME -> handleCreateGame(message);
            case END_TURN -> handleEndTurn(message);
            case SEIZE_COUNTRY -> handleSeizeCountry(message);
            case STRENGTHEN_COUNTRY -> handleStrengthenCountry(message);
            case MOVE_TROOPS -> handleMoveTroops(message);
            case ATTACK -> handleAttack(message);
            default -> handleDefault();
        }
    }

    private void handleUserLeave(WebSocketSession session, WebSocketMessage<?> message) {
        UserLeaveWebsocketMessage userLeaveWebsocketMessage = gson.fromJson((String) message.getPayload(), UserLeaveWebsocketMessage.class);
        GameSession gameSession = gameService.leaveSession(userLeaveWebsocketMessage.getGameSessionId(), session);
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

    public void handleJoin(WebSocketSession session, WebSocketMessage<?> message) {
        JoinWebsocketMessage joinWebsocketMessage = gson.fromJson((String) message.getPayload(), JoinWebsocketMessage.class);
        GameSession gameSession = gameService.joinSession(joinWebsocketMessage.getGameSessionId(), session);
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

    public void handleUserReady(WebSocketSession session, WebSocketMessage<?> message) {
        UserReadyWebsocketMessage userReadyWebsocketMessage = gson.fromJson((String) message.getPayload(), UserReadyWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(userReadyWebsocketMessage.getGameSessionId());
        String userName = Objects.requireNonNull(session.getPrincipal()).getName();
        UserState currentState = gameSession.getUserState(userName);
        currentState.setIsReady(userReadyWebsocketMessage.getIsReady());
        UserSyncWebsocketMessage userSyncWebsocketMessage = new UserSyncWebsocketMessage(gameSession.getReadyUsers());
        sendMessageToAll(gameSession, userSyncWebsocketMessage);
    }

    public void handleCreateGame(WebSocketMessage<?> message) throws Exception {
        CreateGameWebsocketMessage createGameWebsocketMessage = gson.fromJson((String) message.getPayload(), CreateGameWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(createGameWebsocketMessage.getGameSessionId());
        gameSession.createGame(createGameWebsocketMessage.getPlayers());
        GameStartedWebsocketMessage gameStartedWebsocketMessage = new GameStartedWebsocketMessage(new ArrayList<>(gameSession.getPlayers()), gameSession.getActivePlayer());
        sendMessageToAll(gameSession, gameStartedWebsocketMessage);

        GameSyncWebsocketMessage gameSync = new GameSyncWebsocketMessage(gameSession.getRiskController().getBoard());
        sendMessageToAll(gameSession, gameSync);
    }

    public void handleEndTurn(WebSocketMessage<?> message) throws Exception {
        EndTurnWebsocketMessage endTurnWebsocketMessage = gson.fromJson((String) message.getPayload(), EndTurnWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(endTurnWebsocketMessage.getGameSessionId());
        Player activePlayer = gameSession.endTurn();
        gameSession.getNewTroops(activePlayer.getId());
        NewTurnWebsocketMessage newTurnWebsocketMessage = new NewTurnWebsocketMessage(activePlayer);
        sendMessageToAll(gameSession, newTurnWebsocketMessage);
    }

    public void handleSeizeCountry(WebSocketMessage<?> message) throws Exception {
        SeizeCountryWebsocketMessage seizeCountryWebsocketMessage = gson.fromJson((String) message.getPayload(), SeizeCountryWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(seizeCountryWebsocketMessage.getGameSessionId());
        gameSession.seizeCountry(seizeCountryWebsocketMessage.getPlayerId(), seizeCountryWebsocketMessage.getCountryName(), seizeCountryWebsocketMessage.getNumberOfTroops());
        CountryChangedWebsocketMessage countryChangedWebsocketMessage = new CountryChangedWebsocketMessage(seizeCountryWebsocketMessage.getPlayerId(), seizeCountryWebsocketMessage.getCountryName(), seizeCountryWebsocketMessage.getNumberOfTroops());
        sendMessageToAll(gameSession, countryChangedWebsocketMessage);
        Player activePlayer = gameSession.endTurn();
        NewTurnWebsocketMessage newTurnWebsocketMessage = new NewTurnWebsocketMessage(activePlayer);
        sendMessageToAll(gameSession, newTurnWebsocketMessage);
    }

    public void handleStrengthenCountry(WebSocketMessage<?> message) throws Exception {
        StrengthenCountryWebsocketMessage strengthenCountryWebsocketMessage = gson.fromJson((String) message.getPayload(), StrengthenCountryWebsocketMessage.class);
        GameSession gameSession = gameService.getGameSessionById(strengthenCountryWebsocketMessage.getGameSessionId());
        int numberOfTroops = gameSession.strengthenCountry(strengthenCountryWebsocketMessage.getPlayerId(), strengthenCountryWebsocketMessage.getCountryName(), strengthenCountryWebsocketMessage.getNumberOfTroops());
        CountryChangedWebsocketMessage countryChangedWebsocketMessage = new CountryChangedWebsocketMessage(strengthenCountryWebsocketMessage.getPlayerId(), strengthenCountryWebsocketMessage.getCountryName(), numberOfTroops);
        sendMessageToAll(gameSession, countryChangedWebsocketMessage);
    }

    public void handleMoveTroops(WebSocketMessage<?> message) throws Exception {
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

    public void handleAttack(WebSocketMessage<?> message) throws Exception {
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
                sendMessageToAll(gameSession, playerWonWebsocketMessage);
            }
            else
            {
                PlayerEliminatedWebsocketMessage playerEliminatedWebsocketMessage = new PlayerEliminatedWebsocketMessage(attackWebsocketMessage.getDefenderPlayerId());
                sendMessageToAll(gameSession, playerEliminatedWebsocketMessage);
            }
        }
    }

    public void handleDefault() throws RiskException
    {
        throw new RiskException("custom", "action not handled by server");
    }
}
