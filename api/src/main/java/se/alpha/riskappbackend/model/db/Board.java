package se.alpha.riskappbackend.model.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import se.alpha.riskappbackend.model.exception.RiskException;
import se.alpha.riskappbackend.util.GameSetupFactory;

@Getter
public class Board implements Serializable {
    private final List<Continent> continents;
    private List<RiskCard> cards;

    public Board(List<Continent> continents, List<RiskCard> cards) {
        this.continents = continents;
        this.cards = cards;
    }

    public RiskCard getNewRiskCard() throws RiskException
    {
        if(cards.isEmpty())
            cards = GameSetupFactory.getRiskCards();
        return cards.remove(0);
    }

    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();

        for(Continent continent : continents) {
            countries.addAll(continent.getCountries());
        }

        return countries;
    }


}
