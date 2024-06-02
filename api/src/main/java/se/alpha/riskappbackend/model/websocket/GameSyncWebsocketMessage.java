package se.alpha.riskappbackend.model.websocket;

import lombok.Getter;
import se.alpha.riskappbackend.model.db.Board;

@Getter
public class GameSyncWebsocketMessage implements IGameWebsocketMessage {
    private final CustomWebsocketMessageType type = CustomWebsocketMessageType.GAME;
    private final GameWebsocketMessageAction action = GameWebsocketMessageAction.GAME_SYNC;
    private final Board board;

    public GameSyncWebsocketMessage(Board board) {
        this.board = board;
    }

    @Override
    public CustomWebsocketMessageType getType() {
        return type;
    }

    @Override
    public GameWebsocketMessageAction getAction() {
        return action;
    }
}
