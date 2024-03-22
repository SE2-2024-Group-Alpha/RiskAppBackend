package se2.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se2.alpha.riskappbackend.service.GameService;

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
}