package se2.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskCardType;
import se2.alpha.riskappbackend.model.db.RiskController;
import se2.alpha.riskappbackend.model.db.Troop;
import se2.alpha.riskappbackend.model.db.TroopType;
import se2.alpha.riskappbackend.model.game.CreateLobbyRequest;
import se2.alpha.riskappbackend.model.game.CreateLobbyResponse;
import se2.alpha.riskappbackend.service.DiceService;
import se2.alpha.riskappbackend.service.GameService;

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

    @GetMapping("/riskcard")
    public ResponseEntity<?> getNewRiskCard() {
        RiskController riskController = new RiskController();
        try {
            RiskCard riskCard = riskController.getNewRiskCard();
            return ResponseEntity.ok(riskCard);
        }
        catch(Exception ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/riskcard/player/{id}")
    public ResponseEntity<?> getAllRiskCardsByPlayer(@PathVariable("id") String id) {
        RiskController riskController = new RiskController();
        try {
            ArrayList<RiskCard> riskCards = riskController.getRiskCardsByPlayer(id);
            return ResponseEntity.ok(riskCards);
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