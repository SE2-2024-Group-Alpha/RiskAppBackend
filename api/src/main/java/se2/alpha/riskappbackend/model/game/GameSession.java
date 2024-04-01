package se2.alpha.riskappbackend.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

public class GameSession {
    @Getter
    private UUID sessionId;
    @Getter
    private String name;
    @Getter
    private Integer users;
    @Getter
    private int shortId;
    @Getter
    private GameState state;
    private final Map<String, UserState> userStates;

    public GameSession(String name) {
        this.name = name;
        this.sessionId = UUID.randomUUID();
        this.shortId = new Random().nextInt(9000) + 1000; //gets a random number between 1000 and 9999
        this.userStates = new HashMap<>();
        this.state = GameState.LOBBY;
        users = 0;
    }

//    public GameSession(String name, WebSocketSession userSession) {
//        this.name = name;
//        this.sessionId = UUID.randomUUID();
//        this.userSessions = new HashMap<>();
//        this.userSessions.put(userSession.getId(), userSession);
//        this.state = GameState.Lobby;
//        users = 1;
//    }

    public void join(WebSocketSession userSession) {
        String userName = Objects.requireNonNull(userSession.getPrincipal()).getName();
        UserState userState = new UserState(userSession, false);
        userStates.put(userName, userState);
        users = userStates.size();
    }

    public void leave(WebSocketSession userSession) {
        userStates.remove(Objects.requireNonNull(userSession.getPrincipal()).getName());
        users = userStates.size();
    }

    @JsonIgnore
    public List<String> getUserNames() {
        return userStates.keySet().stream().toList();
    }

    @JsonIgnore
    public Map<String, WebSocketSession> getUserSessions() {
        Map<String, WebSocketSession> userSessions = new HashMap<>();
        userStates.forEach((userName, userState) -> userSessions.put(userName, userState.getUserSession()));
        return Collections.unmodifiableMap(userSessions);
    }

    @JsonIgnore
    public Map<String, Boolean> getReadyUsers() {
        Map<String, Boolean> userSessions = new HashMap<>();
        userStates.forEach((userName, userState) -> userSessions.put(userName, userState.getIsReady()));
        return Collections.unmodifiableMap(userSessions);
    }

//    @JsonIgnore
//    public void updateUserState(String userName, UserState userState){
//        userStates.put(userName, userState);
//    }

    @JsonIgnore
    public UserState getUserState(String userName){
        return userStates.get(userName);
    }
}
