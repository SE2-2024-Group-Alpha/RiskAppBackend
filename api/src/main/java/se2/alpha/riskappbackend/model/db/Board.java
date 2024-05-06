package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import se2.alpha.riskappbackend.util.GameSetupFactory;
import se2.alpha.riskappbackend.util.Territories;
import se2.alpha.riskappbackend.util.TerritoryNode;

@Getter
public class Board {
    private ArrayList<Continent> continents;
    private ArrayList<RiskCard> cards;

    public Board() throws Exception{
        continents = GameSetupFactory.getContinents();
        cards = GameSetupFactory.getRiskCards();
    }

    public void setContinents(ArrayList<Continent> continents) {
        this.continents = continents;
    }

    public void setCards(ArrayList<RiskCard> cards) {
        this.cards = cards;
    }

    public RiskCard getNewRiskCard() throws Exception
    {
        if(cards.isEmpty())
            throw new Exception("No available risk cards anymore");
        return cards.remove(0);
    }


}
