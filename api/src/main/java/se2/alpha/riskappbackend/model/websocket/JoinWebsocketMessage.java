package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

import java.util.UUID;

@Getter
public class JoinWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    GameWebsocketMessageAction action = GameWebsocketMessageAction.JOIN;
    private UUID gameSessionId;

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
