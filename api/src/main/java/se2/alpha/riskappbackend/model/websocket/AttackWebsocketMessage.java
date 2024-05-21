package se2.alpha.riskappbackend.model.websocket;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AttackWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.ATTACK;
    private UUID gameSessionId;
    private String attackerPlayerId;
    private String defenderPlayerId;
    private String attackingCountryName;
    private String defendingCountryName;
}
