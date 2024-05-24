package se2.alpha.riskappbackend.model.websocket;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se2.alpha.riskappbackend.model.db.Player;

@AllArgsConstructor
@Getter
public class SeizeCountryWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.SEIZE_COUNTRY;
    private UUID gameSessionId;
    private String playerId;
    private String countryName;
    private int numberOfTroops;
}
