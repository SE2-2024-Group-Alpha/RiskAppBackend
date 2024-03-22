package se2.alpha.riskappbackend.model.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@AllArgsConstructor
public class UserState {
    private WebSocketSession userSession;
    private Boolean isReady;
}
