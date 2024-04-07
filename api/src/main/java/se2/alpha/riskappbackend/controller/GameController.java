package se2.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se2.alpha.riskappbackend.model.game.CreateLobbyRequest;
import se2.alpha.riskappbackend.model.game.CreateLobbyResponse;
import se2.alpha.riskappbackend.service.GameService;

import java.util.UUID;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/lobby")
    public ResponseEntity<?> getActiveLobbies() {
        return ResponseEntity.ok(gameService.getJoinableSessions());
    }

    @PostMapping("/lobby")
    public ResponseEntity<?> createLobby(@RequestBody CreateLobbyRequest request) {
        UUID sessionId = gameService.createNewSession(request.getLobbyName());
        return ResponseEntity.ok(new CreateLobbyResponse(sessionId));
        
    }
}