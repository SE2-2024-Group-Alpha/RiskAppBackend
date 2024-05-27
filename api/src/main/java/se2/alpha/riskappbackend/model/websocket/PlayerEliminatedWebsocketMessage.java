package se2.alpha.riskappbackend.model.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se2.alpha.riskappbackend.model.db.Player;

@AllArgsConstructor
@Getter
public class PlayerEliminatedWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.PLAYER_ELIMINATED;
    private String eliminatedPlayerId;
}
