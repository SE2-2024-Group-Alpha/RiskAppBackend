package se.alpha.riskappbackend.model.websocket;

import lombok.Getter;
import se.alpha.riskappbackend.model.db.Board;
import se.alpha.riskappbackend.model.db.Country;
import se.alpha.riskappbackend.model.db.Player;
import se.alpha.riskappbackend.model.db.RiskCard;
import se.alpha.riskappbackend.model.game.GameSession;

import java.util.List;

@Getter
public class GameSyncWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.GAME_SYNC;

    private final List<Country> countries;
    private final Player activePlayerName;
    private final List<Player> players;


    public GameSyncWebsocketMessage(GameSession session) {
        this.countries = session.getRiskController().getBoard().getCountries();
        this.players = session.getPlayers();
        this.activePlayerName = session.getActivePlayer();
    }

    @Override
    public CustomWebsocketMessageType getType() {
        return type;
    }

    @Override
    public GameWebsocketMessageAction getAction() {
        return action;
    }
}