package se2.alpha.riskappbackend.model.websocket;

import lombok.Getter;

@Getter
public class CustomWebsocketMessage implements ICustomWebsocketMessage {
    private CustomWebsocketMessageType type;
}
