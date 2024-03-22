package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

public interface ICustomWebsocketMessage {
    CustomWebsocketMessageType getType();
}