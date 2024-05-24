package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;

@Getter
public class RiskController {
    private UUID id;
    private ArrayList<Player> players;
    private Board board;
    private int idxPlayerTurn;

    public RiskController(ArrayList<Player> players, Board board) {
        this.id = UUID.randomUUID();
        this.players = players;
        this.board = board;
        idxPlayerTurn = 0;
        players.get(idxPlayerTurn).setCurrentTurn(true);
    }

    public RiskController() {
    }

    public void endPlayerTurn()
    {
        players.get(idxPlayerTurn).setCurrentTurn(false);
        if(++idxPlayerTurn >= players.size())
            idxPlayerTurn = 0;
        if(players.get(idxPlayerTurn).isEliminated())
            endPlayerTurn();
        else
            players.get(idxPlayerTurn).setCurrentTurn(true);
    }

    public RiskCard getNewRiskCard(String playerId) throws Exception
    {
        Player player = getPlayerById(playerId);
        RiskCard riskCard = board.getNewRiskCard();
        player.addRiskCard(riskCard);
        return riskCard;
    }

    public ArrayList<RiskCard> getRiskCardsByPlayer(String playerId) throws Exception
    {
        for(Player player : players)
        {
            if(player.getId().equals(playerId))
                return player.getCards();
        }
        throw new Exception("no player with this id found");
    }

    public boolean canPlayerTradeRiskCards(String playerId) throws Exception
    {
        Player player = getPlayerById(playerId);
        return !player.canTradeRiskCards().equals(TradeType.NONE);
    }

    public void tradeRiskCards(String playerId) throws Exception
    {
        Player player = getPlayerById(playerId);
        TradeType tradeType = player.canTradeRiskCards();
        if(tradeType.equals(TradeType.NONE))
            throw new Exception("Player cannot trade any risk cards");
        player.tradeRiskCards();
    }

    public void moveTroops(String playerId, String moveFromCountryName, String moveToCountryName, int cntTroops) throws Exception {
        Player player = getPlayerById(playerId);
        Country moveFromCountry = getCountryByName(moveFromCountryName);
        Country moveToCountry = getCountryByName(moveToCountryName);
        if(!player.equals(moveFromCountry.getOwner()) || !player.equals(moveToCountry.getOwner()))
            throw new Exception("countries must be owned by player");
        if(moveFromCountry.getNumberOfTroops() <= cntTroops)
            throw new Exception("not enough troops in this country to move from");
        if(!moveFromCountry.getAttackableCountries().contains(moveToCountry))
            throw new Exception("moving troops between those 2 countries not allowed");

        moveFromCountry.removeArmy(cntTroops);
        moveToCountry.addArmy(cntTroops);
    }

    public int strengthenCountry(String playerId, String countryName, int cntTroops) throws Exception
    {
        Player player = getPlayerById(playerId);
        Country country = getCountryByName(countryName);
        if(!player.equals(country.getOwner()))
            throw new Exception("Country not owned by player");

        player.strengthenCountry(country, cntTroops);
        return country.getNumberOfTroops();
    }

    public void seizeCountry(String playerId, String countryName, int cntTroops) throws Exception
    {
        Player player = getPlayerById(playerId);
        Country country = getCountryByName(countryName);
        if(country.getOwner() != null)
            throw new Exception("Country cannot be seized while being owned by another player");

        player.seizeCountry(country, cntTroops);
    }

    public void getNewTroops(String playerId) throws Exception
    {
        int cntNewTroops = 0;
        Player player = getPlayerById(playerId);
        cntNewTroops = player.getControlledCountries().size() / 3;
        cntNewTroops += player.getControlledContinents().size() * 5;

        if(cntNewTroops < 3)
            cntNewTroops = 3;

        player.addArmy(cntNewTroops);
    }

    public void attack(String attackerPlayerId, String defenderPlayerId, String attackingCountryName, String defendingCountryName) throws Exception
    {
        int attackerLosses = 0;
        int defenderLosses = 0;
        Player attacker = getPlayerById(attackerPlayerId);
        Player defender = getPlayerById(defenderPlayerId);
        Country attackingCountry = getCountryByName(attackingCountryName);
        Country defendingCountry = getCountryByName(defendingCountryName);

        if(!attackingCountry.getAttackableCountries().contains(defendingCountry))
            throw new Exception("Cannot attack this country");

        if(attackingCountry.getNumberOfTroops() < 2)
            throw new Exception("Attacking Country must have at least 2 troops");

        Integer[] attackerRolls = Dice.rollMultipleTimes((Integer) (attackingCountry.getNumberOfTroops() - 1));
        Integer[] defenderRolls = Dice.rollMultipleTimes((Integer) defendingCountry.getNumberOfTroops());

        for(int i = 0; i < Math.min(defenderRolls.length, attackerRolls.length); i++)
        {
            if(attackerRolls[i] > defenderRolls[i])
            {
                defenderLosses++;
            }
            else
            {
                attackerLosses++;
            }
        }

        boolean attackSuccessful = processDefender(defender, defendingCountry, defenderLosses);
        processAttacker(attacker, attackingCountry, defendingCountry, attackerLosses, attackSuccessful);

        if(!attackSuccessful && attackingCountry.getNumberOfTroops() > 1)
            attack(attackerPlayerId, defenderPlayerId, attackingCountryName, defendingCountryName);
    }

    public Player getActivePlayer()
    {
        for(Player player : players)
            if(player.isCurrentTurn())
                return player;
        return null;
    }

    private Player getPlayerById(String id) throws Exception
    {
        for(Player player : players)
        {
            if(player.getId().equals(id))
                return player;
        }
        throw new Exception("no player with this id found");
    }

    public Country getCountryByName(String countryName) throws Exception
    {
        for(Continent continent : board.getContinents())
        {
            for(Country country : continent.getCountries())
                if(countryName.equals(country.getName()))
                    return country;
        }
        throw new Exception("no country with this name found");
    }

    private boolean processDefender(Player player, Country country, int losses)
    {
        processLosses(player, country, losses);

        if(country.getNumberOfTroops() == 0) {
            player.loseControlOverCountry(country);
            if(player.equals(country.getContinent().getOwner()))
                player.loseControlOverContinent(country.getContinent());
        }

        return country.getNumberOfTroops() == 0;
    }

    private static void processLosses(Player player, Country country, int losses) {
            country.removeArmy(losses);
            player.removeArmy(losses);
    }

    private void processAttacker(Player player, Country attackingCountry, Country defendingCountry, int losses, boolean attackSuccessful)
    {
        processLosses(player, attackingCountry, losses);
        if(attackSuccessful)
        {
            if(attackingCountry.getNumberOfTroops() > 1) {
                defendingCountry.addArmy(attackingCountry.getNumberOfTroops() - 1);
                attackingCountry.removeArmy(attackingCountry.getNumberOfTroops() - 1);
                player.controlCountry(defendingCountry);
            }

            if(isContinentOwnedByPlayer(defendingCountry.getContinent(), player))
                player.controlContinent(defendingCountry.getContinent());
        }
    }
    private boolean isContinentOwnedByPlayer(Continent continent, Player player)
    {
        for(Country country : continent.getCountries())
        {
            if (!player.equals(country.getOwner())) {
                return false;
            }
        }
        return true;
    }
}
