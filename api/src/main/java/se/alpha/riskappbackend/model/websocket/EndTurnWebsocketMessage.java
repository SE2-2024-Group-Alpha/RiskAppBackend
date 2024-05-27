package se.alpha.riskappbackend.model.websocket;

import java.util.UUID;

import lombok.Getter;

@Getter
public class EndTurnWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    GameWebsocketMessageAction action = GameWebsocketMessageAction.END_TURN;
    private UUID gameSessionId;
}
