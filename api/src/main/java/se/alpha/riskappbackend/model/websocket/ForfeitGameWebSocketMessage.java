package se.alpha.riskappbackend.model.websocket;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ForfeitGameWebSocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.FORFEIT_GAME;
    private UUID gameSessionId;

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
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
