package se.alpha.riskappbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.websocket.RiskWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private GameService gameService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(riskSocketHandler(), "/socket");
    }

    public WebSocketHandler riskSocketHandler() {
        return new RiskWebSocketHandler(gameService);
    }
}
