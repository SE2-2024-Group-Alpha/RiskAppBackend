package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserLeaveWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    GameWebsocketMessageAction action = GameWebsocketMessageAction.USER_LEAVE;
    private UUID gameSessionId;
}
