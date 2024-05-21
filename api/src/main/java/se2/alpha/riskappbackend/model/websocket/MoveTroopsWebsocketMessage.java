package se2.alpha.riskappbackend.model.websocket;

import java.util.ArrayList;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se2.alpha.riskappbackend.model.db.Player;

@AllArgsConstructor
@Getter
public class MoveTroopsWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.MOVE_TROOPS;
    private UUID gameSessionId;
    private String playerId;
    private String moveFromCountryName;
    private String moveToCountryName;
    private int numberOfTroops;
}
