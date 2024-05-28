package se.alpha.riskappbackend.model.websocket;

public interface IGameWebsocketMessage extends ICustomWebsocketMessage {
    GameWebsocketMessageAction getAction();
}