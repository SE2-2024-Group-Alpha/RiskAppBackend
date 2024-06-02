package se.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private static final int NUMBER_OF_TROOPS_THREE_PLAYER_GAME = 35;
    private static final int NUMBER_OF_TROOPS_FOUR_PLAYER_GAME = 30;
    private static final int NUMBER_OF_TROOPS_FIVE_PLAYER_GAME = 25;
    private static final int NUMBER_OF_TROOPS_SIX_PLAYER_GAME = 20;
    private static final String CUSTOM_EXCEPTION_TYPE = "custom";

    private GameSetupFactory() {

    }

    public static RiskController setupThreePlayerGame(List<Player> players) throws RiskException {
        for (Player player : players)
            player.addArmy(NUMBER_OF_TROOPS_THREE_PLAYER_GAME);
        Board board = new Board(getContinents(), getRiskCards());

        for (Player player : players)
            playerTerritoryStartup(player, board);

        return new RiskController(players, board);
    }

    public static RiskController setupFourPlayerGame(List<Player> players) throws RiskException {
        if (players.size() != 4)
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "there must be 4 players");
        for (Player player : players)
            player.addArmy(NUMBER_OF_TROOPS_FOUR_PLAYER_GAME);
        Board board = new Board(getContinents(), getRiskCards());

        for (Player player : players)
            playerTerritoryStartup(player, board);

        return new RiskController(players, board);
    }

    public static RiskController setupFivePlayerGame(List<Player> players) throws RiskException {
        if (players.size() != 5)
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "there must be 5 players");
        for (Player player : players)
            player.addArmy(NUMBER_OF_TROOPS_FIVE_PLAYER_GAME);
        Board board = new Board(getContinents(), getRiskCards());

        for (Player player : players)
            playerTerritoryStartup(player, board);

        return new RiskController(players, board);
    }

    public static RiskController setupSixPlayerGame(List<Player> players) throws RiskException {
        if (players.size() != 6)
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "there must be 6 players");
        for (Player player : players)
            player.addArmy(NUMBER_OF_TROOPS_SIX_PLAYER_GAME);
        Board board = new Board(getContinents(), getRiskCards());

        for (Player player : players)
            playerTerritoryStartup(player, board);

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

    private static void setupContinents() throws RiskException {
        HashMap<String, TerritoryNode> territories = (HashMap<String, TerritoryNode>) Territories.getAllTerritories();
        continents = new ArrayList<>();
        for (Map.Entry<String, TerritoryNode> entry : territories.entrySet()) {
            String territoryName = entry.getKey();
            TerritoryNode territoryNode = entry.getValue();
            if (isNewContinent(territoryNode.getContinent()))
                continents.add(new Continent(territoryNode.getContinent(), null));
            setupCountry(territoryNode, territoryName);
        }

        setAttackableCountries(territories);
    }

    private static void playerTerritoryStartup(Player player, Board board) {
        int troopsToAssign = player.getFreeNumberOfTroops() / 2;
        int assignPerTurn = troopsToAssign / 4;
        int i = 0;
        while (i < 4) {
            Random random = new Random();
            List<Continent> continents = board.getContinents();
            int randomIndex = random.nextInt(continents.size());
            Continent continent = continents.get(randomIndex);

            List<Country> countries = continent.getCountries();
            randomIndex = random.nextInt(countries.size());
            Country country = countries.get(randomIndex);
            if (country.getOwner() == null) {
                country.setOwner(player);
                country.addArmy(assignPerTurn);
                i++;
            }
        }
    }

    private static void setAttackableCountries(HashMap<String, TerritoryNode> territories) throws RiskException {
        for (Country country : getAllCountries()) {
            for (TerritoryNode territoryNode : territories.get(country.getName()).getAdjTerritories()) {
                country.addAttackableCountry(getCountryByName(territoryNode.getName()));
            }

        }
    }

    private static void setupCountry(TerritoryNode territoryNode, String territoryName) throws RiskException {
        Continent continent = getContinentByName(territoryNode.getContinent());
        continent.addCountry(new Country(territoryName, null, continent));
    }

    private static boolean isNewContinent(String name) {
        for (Continent continent : continents) {
            if (continent.getName().equals(name))
                return false;
        }
        return true;
    }

    private static Continent getContinentByName(String name) throws RiskException {
        for (Continent continent : continents) {
            if (continent.getName().equals(name))
                return continent;
        }
        throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Continent not found");
    }

    private static void setupRiskCards() throws RiskException {
        riskCards = new ArrayList<>();
        if (continents == null)
            setupContinents();
        ArrayList<Country> countries = getAllCountries();

        for (int i = 0; i < countries.size(); i++) {
            if (i < countries.size() / 3)
                riskCards.add(setupArtilleryRiskCard(countries.get(i)));
            else if (i < countries.size() / 3 * 2)
                riskCards.add(setupCavalryRiskCard(countries.get(i)));
            else
                riskCards.add(setuInfantryRiskCard(countries.get(i)));
        }

        setupJokerRiskCards();

        Collections.shuffle(riskCards);
    }

    private static void setupJokerRiskCards() {
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
        riskCards.add(new RiskCard(RiskCardType.JOKER, null));
    }

    private static RiskCard setupArtilleryRiskCard(Country country) {
        return new RiskCard(RiskCardType.ARTILLERY, country);
    }

    private static RiskCard setuInfantryRiskCard(Country country) {
        return new RiskCard(RiskCardType.INFANTRY, country);
    }

    private static RiskCard setupCavalryRiskCard(Country country) {
        return new RiskCard(RiskCardType.CAVALRY, country);
    }

    private static ArrayList<Country> getAllCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        for (Continent continent : continents) {
            countries.addAll(continent.getCountries());
        }
        return countries;
    }

    private static Country getCountryByName(String name) throws RiskException {
        for (Country country : getAllCountries()) {
            if (country.getName().equals(name))
                return country;
        }
        throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Country not found");
    }
}
