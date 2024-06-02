package se.alpha.riskappbackend.model.websocket;

import lombok.Getter;

@Getter
public class CustomWebsocketMessage implements ICustomWebsocketMessage {
    private CustomWebsocketMessageType type;




    public void setType(CustomWebsocketMessageType type) {
        this.type = type;
    }
}
