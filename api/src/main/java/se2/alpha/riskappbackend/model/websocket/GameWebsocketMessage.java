package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

@Getter
public class GameWebsocketMessage implements IGameWebsocketMessage {
    CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    GameWebsocketMessageAction action;
}
