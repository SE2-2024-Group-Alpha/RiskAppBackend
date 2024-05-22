package se2.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import se2.alpha.riskappbackend.model.db.Board;
import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.Player;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskCardType;
import se2.alpha.riskappbackend.model.db.RiskController;

public class GameSetupFactory {
    private static ArrayList<Continent> continents;
    private static ArrayList<RiskCard> riskCards;
    private static int NUMBEROFTROOPSTHREEPLAYERGAME = 35;
    private static int NUMBEROFTROOPSFOURPLAYERGAME = 30;
    private static int NUMBEROFTROOPSFIVEPLAYERGAME = 25;
    private static int NUMBEROFTROOPSSIXPLAYERGAME = 20;

    public static RiskController setupThreePlayerGame(ArrayList<Player> players) throws Exception {
        if(players.size() != 3)
            throw new Exception("there must be 3 players");
        for(Player player : players)
            player.addArmy(NUMBEROFTROOPSTHREEPLAYERGAME);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    public static RiskController setupFourPlayerGame(ArrayList<Player> players) throws Exception {
        if(players.size() != 4)
            throw new Exception("there must be 4 players");
        for(Player player : players)
            player.addArmy(NUMBEROFTROOPSFOURPLAYERGAME);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    public static RiskController setupFivePlayerGame(ArrayList<Player> players) throws Exception {
        if(players.size() != 5)
            throw new Exception("there must be 5 players");
        for(Player player : players)
            player.addArmy(NUMBEROFTROOPSFIVEPLAYERGAME);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }

    public static RiskController setupSixPlayerGame(ArrayList<Player> players) throws Exception {
        if(players.size() != 6)
            throw new Exception("there must be 6 players");
        for(Player player : players)
            player.addArmy(NUMBEROFTROOPSSIXPLAYERGAME);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    private static ArrayList<Continent> getContinents() throws Exception {
        setupContinents();
        return continents;
    }

    private static ArrayList<RiskCard> getRiskCards() throws Exception {
        setupRiskCards();
        return riskCards;
    }
    private static void setupContinents() throws Exception
    {
        HashMap<String, TerritoryNode> territories = Territories.getAllTerritories();
        continents = new ArrayList<Continent>();
        for (Map.Entry<String, TerritoryNode> entry : territories.entrySet()) {
            String territoryName = entry.getKey();
            TerritoryNode territoryNode = entry.getValue();
            if(isNewContinent(territoryNode.getContinent()))
                continents.add(new Continent(territoryNode.getContinent(), null));
            setupCountry(territoryNode, territoryName);
        }

        setAttackableCountries(territories);
    }

    private static void setAttackableCountries(HashMap<String, TerritoryNode> territories) throws Exception {
        for(Country country : getAllCountries())
        {
            for(TerritoryNode territoryNode : territories.get(country.getName()).getAdjTerritories())
            {
                country.addAttackableCountry(getCountryByName(territoryNode.getName()));
            }

        }
    }

    private static void setupCountry(TerritoryNode territoryNode, String territoryName) throws Exception {
        Continent continent = getContinentByName(territoryNode.getContinent());
        continent.addCountry(new Country(territoryName, null, continent));
    }

    private static boolean isNewContinent(String name)
    {
        for(Continent continent : continents)
        {
            if(continent.getName().equals(name))
                return false;
        }
        return true;
    }

    private static Continent getContinentByName(String name) throws Exception
    {
        for(Continent continent : continents)
        {
            if(continent.getName().equals(name))
                return continent;
        }
        throw new Exception("Continent not found");
    }

    private static void setupRiskCards() throws Exception {
        riskCards = new ArrayList<RiskCard>();
        if(continents == null)
            setupContinents();
        ArrayList<Country> countries = getAllCountries();

        for(int i = 0; i < countries.size(); i++)
        {
            if(i < countries.size() / 3)
                riskCards.add(setupArtilleryRiskCard(countries.get(i)));
            else if(i < countries.size() / 3 * 2)
                riskCards.add(setupCavalryRiskCard(countries.get(i)));
            else
                riskCards.add(setuInfantryRiskCard(countries.get(i)));
        }

        setupJokerRiskCards();

        Collections.shuffle(riskCards);
    }

    private static void setupJokerRiskCards()
    {
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
    }
    private static RiskCard setupArtilleryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.ARTILLERY, country);
    }
    private static RiskCard setuInfantryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.INFANTRY, country);
    }
    private static RiskCard setupCavalryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.CAVALRY, country);
    }
    private static ArrayList<Country> getAllCountries()
    {
        ArrayList<Country> countries = new ArrayList<Country>();
        for(Continent continent : continents)
        {
            countries.addAll(continent.getCountries());
        }
        return countries;
    }

    private static Country getCountryByName(String name) throws Exception
    {
        for(Country country : getAllCountries())
        {
            if(country.getName().equals(name))
                return country;
        }
        throw new Exception("Country not found");
    }
}
