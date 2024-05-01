package se2.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskCardType;
import se2.alpha.riskappbackend.model.db.Troop;
import se2.alpha.riskappbackend.model.db.TroopType;
import se2.alpha.riskappbackend.model.game.CreateLobbyRequest;
import se2.alpha.riskappbackend.model.game.CreateLobbyResponse;
import se2.alpha.riskappbackend.service.GameService;

import java.util.ArrayList;
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

    @GetMapping("/riskcard")
    public ResponseEntity<?> getNewRiskCard() {
        return ResponseEntity.ok(new RiskCard(RiskCardType.ARTILLERY, new Country("Austria", null, new Continent("Europe", null))));
    }

    @GetMapping("/riskcard/player/{id}")
    public ResponseEntity<?> getAllRiskCardsByPlayer(@PathVariable("id") String id) {
        ArrayList<RiskCard> riskCards = new ArrayList<RiskCard>();
        riskCards.add(new RiskCard(RiskCardType.ARTILLERY, new Country("Austria", null, new Continent("Europe", null))));
        riskCards.add(new RiskCard(RiskCardType.CAVALRY, new Country("India", null, new Continent("Europe", null))));
        riskCards.add(new RiskCard(RiskCardType.INFANTRY, new Country("Australia", null, new Continent("Europe", null))));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        return ResponseEntity.ok(riskCards);
    }
}