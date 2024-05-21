package se2.alpha.riskappbackend.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import se2.alpha.riskappbackend.model.db.Player;
import se2.alpha.riskappbackend.model.db.RiskController;
import se2.alpha.riskappbackend.util.GameSetupFactory;

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
    private RiskController riskController;

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

    public void createGame(ArrayList<Player> players) throws Exception
    {
        switch(players.size())
        {
            case 3:
                riskController = GameSetupFactory.setupThreePlayerGame(players);
                break;
            case 4:
                riskController = GameSetupFactory.setupFourPlayerGame(players);
                break;
            case 5:
                riskController = GameSetupFactory.setupFivePlayerGame(players);
                break;
            case 6:
                riskController = GameSetupFactory.setupSixPlayerGame(players);
                break;
            default:
                throw new Exception("there must be between 3 and 6 players");
        }
    }

    public Player endTurn() throws Exception
    {
        riskController.endPlayerTurn();
        return riskController.getActivePlayer();
    }

    public void seizeCountry(String playerId, String countryName, int numberOfTroops) throws Exception
    {
        riskController.seizeCountry(playerId, countryName, numberOfTroops);
    }

    public int strengthenCountry(String playerId, String countryName, int numberOfTroops) throws Exception
    {
        return riskController.strengthenCountry(playerId, countryName, numberOfTroops);
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

    public boolean isEmpty() {
        return userStates.isEmpty();
    }

    @JsonIgnore
    public Player getActivePlayer()
    {
        return riskController.getActivePlayer();
    }

    @JsonIgnore
    public ArrayList<Player> getPlayers()
    {
        return riskController.getPlayers();
    }
}
