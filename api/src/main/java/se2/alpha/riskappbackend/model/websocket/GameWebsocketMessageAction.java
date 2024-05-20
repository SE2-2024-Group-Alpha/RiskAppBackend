package se2.alpha.riskappbackend.model.websocket;

public enum GameWebsocketMessageAction {
    JOIN,
    USER_SYNC,
    USER_READY,
    USER_LEAVE,
    CREATE_GAME,
    GAME_STARTED,
    END_TURN,
    NEW_TURN
}
