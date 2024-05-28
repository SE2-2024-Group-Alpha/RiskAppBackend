package se.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.alpha.riskappbackend.model.db.Board;
import se.alpha.riskappbackend.model.db.Continent;
import se.alpha.riskappbackend.model.db.Country;
import se.alpha.riskappbackend.model.db.Player;
import se.alpha.riskappbackend.model.db.RiskCard;
import se.alpha.riskappbackend.model.db.RiskCardType;
import se.alpha.riskappbackend.model.db.RiskController;
import se.alpha.riskappbackend.model.exception.RiskException;

public class GameSetupFactory {
    private static ArrayList<Continent> continents;
    private static ArrayList<RiskCard> riskCards;
    private static int numberOfTroopsThreePlayerGame = 35;
    private static int numberOfTroopsFourPlayerGame = 30;
    private static int numberOfTroopsFivePlayerGame = 25;
    private static int numberOfTroopsSixPlayerGame = 20;
    private static String customExceptionType = "custom";

    private GameSetupFactory()
    {

    }

    public static RiskController setupThreePlayerGame(List<Player> players) throws RiskException {
        if(players.size() != 3)
            throw new RiskException(customExceptionType, "there must be 3 players");
        for(Player player : players)
            player.addArmy(numberOfTroopsThreePlayerGame);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    public static RiskController setupFourPlayerGame(List<Player> players) throws RiskException {
        if(players.size() != 4)
            throw new RiskException(customExceptionType, "there must be 4 players");
        for(Player player : players)
            player.addArmy(numberOfTroopsFourPlayerGame);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    public static RiskController setupFivePlayerGame(List<Player> players) throws RiskException {
        if(players.size() != 5)
            throw new RiskException(customExceptionType, "there must be 5 players");
        for(Player player : players)
            player.addArmy(numberOfTroopsFivePlayerGame);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }

    public static RiskController setupSixPlayerGame(List<Player> players) throws RiskException {
        if(players.size() != 6)
            throw new RiskException(customExceptionType, "there must be 6 players");
        for(Player player : players)
            player.addArmy(numberOfTroopsSixPlayerGame);
        Board board = new Board(getContinents(), getRiskCards());
        return new RiskController(players, board);
    }
    private static ArrayList<Continent> getContinents() throws RiskException {
        setupContinents();
        return continents;
    }

    public static ArrayList<RiskCard> getRiskCards() throws RiskException {
        setupRiskCards();
        return riskCards;
    }
    private static void setupContinents() throws RiskException
    {
        HashMap<String, TerritoryNode> territories = Territories.getAllTerritories();
        continents = new ArrayList<>();
        for (Map.Entry<String, TerritoryNode> entry : territories.entrySet()) {
            String territoryName = entry.getKey();
            TerritoryNode territoryNode = entry.getValue();
            if(isNewContinent(territoryNode.getContinent()))
                continents.add(new Continent(territoryNode.getContinent(), null));
            setupCountry(territoryNode, territoryName);
        }

        setAttackableCountries(territories);
    }

    private static void setAttackableCountries(HashMap<String, TerritoryNode> territories) throws RiskException {
        for(Country country : getAllCountries())
        {
            for(TerritoryNode territoryNode : territories.get(country.getName()).getAdjTerritories())
            {
                country.addAttackableCountry(getCountryByName(territoryNode.getName()));
            }

        }
    }

    private static void setupCountry(TerritoryNode territoryNode, String territoryName) throws RiskException {
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

    private static Continent getContinentByName(String name) throws RiskException
    {
        for(Continent continent : continents)
        {
            if(continent.getName().equals(name))
                return continent;
        }
        throw new RiskException(customExceptionType, "Continent not found");
    }

    private static void setupRiskCards() throws RiskException {
        riskCards = new ArrayList<>();
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
        ArrayList<Country> countries = new ArrayList<>();
        for(Continent continent : continents)
        {
            countries.addAll(continent.getCountries());
        }
        return countries;
    }

    private static Country getCountryByName(String name) throws RiskException
    {
        for(Country country : getAllCountries())
        {
            if(country.getName().equals(name))
                return country;
        }
        throw new RiskException(customExceptionType, "Country not found");
    }
}
