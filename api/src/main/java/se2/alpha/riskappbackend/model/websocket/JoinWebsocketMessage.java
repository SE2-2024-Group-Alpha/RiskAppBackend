package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

import java.util.UUID;

@Getter
public class JoinWebsocketMessage extends GameWebsocketMessage {
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.JOIN;
    private UUID gameSessionId;
}
