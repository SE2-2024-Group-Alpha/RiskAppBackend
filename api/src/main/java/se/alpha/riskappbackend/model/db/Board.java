package se.alpha.riskappbackend.model.db;

import java.util.ArrayList;

import lombok.Getter;
import se.alpha.riskappbackend.model.exception.RiskException;
import se.alpha.riskappbackend.util.GameSetupFactory;

@Getter
public class Board {
    private ArrayList<Continent> continents;
    private ArrayList<RiskCard> cards;

    public Board(ArrayList<Continent> continents, ArrayList<RiskCard> cards) {
        this.continents = continents;
        this.cards = cards;
    }

    public RiskCard getNewRiskCard() throws RiskException
    {
        if(cards.isEmpty())
            cards = GameSetupFactory.getRiskCards();
        return cards.remove(0);
    }


}
