package se2.alpha.riskappbackend.model.websocket;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserSyncWebsocketMessage extends GameWebsocketMessage {
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.SYNC_USERS;
    private List<String> userNames;
}
