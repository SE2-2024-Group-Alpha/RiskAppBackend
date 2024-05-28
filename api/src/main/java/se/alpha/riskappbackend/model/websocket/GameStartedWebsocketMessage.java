package se.alpha.riskappbackend.model.websocket;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se.alpha.riskappbackend.model.db.Player;

@AllArgsConstructor
@Getter
public class GameStartedWebsocketMessage implements IGameWebsocketMessage {
    private static final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private static final GameWebsocketMessageAction action = GameWebsocketMessageAction.GAME_STARTED;
    private ArrayList<Player> players;
    private Player activePlayer;

    @Override
    public CustomWebsocketMessageType getType() {
        return type;
    }

    @Override
    public GameWebsocketMessageAction getAction() {
        return action;
    }
}
