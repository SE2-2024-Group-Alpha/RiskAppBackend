package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

@Getter
public class GameWebsocketMessage implements ICustomWebsocketMessage {
    CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    GameWebsocketMessageAction action;
}
