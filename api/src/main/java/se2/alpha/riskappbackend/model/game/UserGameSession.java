package se2.alpha.riskappbackend.model.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;
import se2.alpha.riskappbackend.entity.User;

@Getter
@AllArgsConstructor
public class UserGameSession {
    private WebSocketSession webSocketSession;
    private User user;
}
