package se.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.alpha.riskappbackend.model.db.RiskCard;
import se.alpha.riskappbackend.model.game.CreateLobbyRequest;
import se.alpha.riskappbackend.model.game.CreateLobbyResponse;
import se.alpha.riskappbackend.model.game.GameSession;
import se.alpha.riskappbackend.service.GameService;
import se.alpha.riskappbackend.service.DiceService;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private GameService gameService;
    private DiceService diceService;

    @GetMapping("/lobby")
    public ResponseEntity<?> getActiveLobbies() {
        return ResponseEntity.ok(gameService.getJoinableSessions());
    }

    @PostMapping("/lobby")
    public ResponseEntity<?> createLobby(@RequestBody CreateLobbyRequest request) {
        UUID sessionId = gameService.createNewSession(request.getLobbyName());
        return ResponseEntity.ok(new CreateLobbyResponse(sessionId));
        
    }

    @GetMapping("/{gamesessionid}/player/{id}/riskcard")
    public ResponseEntity<?> getNewRiskCard(@PathVariable String gamesessionid, @PathVariable String id) {
        GameSession gameSession = gameService.getGameSessionById(UUID.fromString(gamesessionid));
        try {
            RiskCard riskCard = gameSession.getNewRiskCard(id);
            return ResponseEntity.ok(riskCard);
        }
        catch(Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{gamesessionid}/player/{id}/riskcards")
    public ResponseEntity<?> getAllRiskCardsByPlayer(@PathVariable String gamesessionid, @PathVariable("id") String id) {
        GameSession gameSession = gameService.getGameSessionById(UUID.fromString(gamesessionid));
        try {
            ArrayList<RiskCard> riskCards = gameSession.getRiskCardsByPlayer(id);
            return ResponseEntity.ok(riskCards);
        }
        catch(Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{gameSessionId}/player/{id}/riskcards/tradable")
    public ResponseEntity<?> canPlayerTradeRiskCards(@PathVariable String gameSessionId, @PathVariable("id") String id) {
        GameSession gameSession = gameService.getGameSessionById(UUID.fromString(gameSessionId));
        try {
            boolean canTrade = gameSession.canPlayerTradeRiskCards(id);
            return ResponseEntity.ok(canTrade);
        }
        catch(Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{gameSessionId}/player/{id}/riskcards/trade")
    public ResponseEntity<?> tradeRiskCards(@PathVariable String gameSessionId, @PathVariable("id") String id) {
        GameSession gameSession = gameService.getGameSessionById(UUID.fromString(gameSessionId));
        try {
            gameSession.tradeRiskCards(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/dice/roll")
    public ResponseEntity<?> rollDice() {
        int result = diceService.roll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dice/rollMultiple/{numRolls}")
    public ResponseEntity<?> rollMultiple(@PathVariable int numRolls) {
        int[] results = diceService.rollMultipleTimes(numRolls);
        return ResponseEntity.ok(results);
    }
}