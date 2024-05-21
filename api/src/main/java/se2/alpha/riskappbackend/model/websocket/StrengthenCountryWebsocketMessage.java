package se2.alpha.riskappbackend.model.websocket;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrengthenCountryWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.STRENGTHEN_COUNTRY;
    private UUID gameSessionId;
    private String playerId;
    private String countryName;
    private int numberOfTroops;
}
