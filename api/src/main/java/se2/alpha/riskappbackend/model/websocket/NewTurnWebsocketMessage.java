package se2.alpha.riskappbackend.model.websocket;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se2.alpha.riskappbackend.model.db.Player;

@AllArgsConstructor
@Getter
public class NewTurnWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.NEW_TURN;
    private Player activePlayer;
}
