package se.alpha.riskappbackend.model.websocket;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserLeaveWebsocketMessage implements IGameWebsocketMessage {
    private static final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private static final GameWebsocketMessageAction action = GameWebsocketMessageAction.USER_LEAVE;
    private UUID gameSessionId;

    @Override
    public CustomWebsocketMessageType getType() {
        return type;
    }

    @Override
    public GameWebsocketMessageAction getAction() {
        return action;
    }
}
