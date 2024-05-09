package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

import se2.alpha.riskappbackend.util.Dice;

public class RiskController {
    private ArrayList<Player> players;
    private Board board;

    public RiskController(ArrayList<Player> players, Board board) {
        this.players = players;
        this.board = board;
    }

    public RiskController() {
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    public void moveTroops(String playerId, Country moveFromCountry, Country moveToCountry, int cntTroops) throws Exception {
        Player player = getPlayerById(playerId);
        if(!player.equals(moveFromCountry.getOwner()) || !player.equals(moveToCountry.getOwner()))
            throw new Exception("countries must be owned by player");
        if(moveFromCountry.getNumberOfTroops() <= cntTroops)
            throw new Exception("not enough troops in this country to move from");
        if(!moveFromCountry.getAttackableCountries().contains(moveToCountry))
            throw new Exception("moving troops between those 2 countries not allowed");

        moveFromCountry.removeArmy(cntTroops);
        moveToCountry.addArmy(cntTroops);
    }

    public void strengthenCountry(String playerId, Country country, int cntTroops) throws Exception
    {
        Player player = getPlayerById(playerId);
        if(!player.equals(country.getOwner()))
            throw new Exception("Country not owned by player");

        player.strengthenCountry(country, cntTroops);
    }

    public void seizeCountry(String playerId, Country country, int cntTroops) throws Exception
    {
        Player player = getPlayerById(playerId);
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

    public void attack(Player attacker, Player defender, Country attackingCountry, Country defendingCountry) throws Exception
    {
        int attackerLosses = 0;
        int defenderLosses = 0;
        if(!attackingCountry.getAttackableCountries().contains(defendingCountry))
            throw new Exception("Cannot attack this country");

        if(attackingCountry.getNumberOfTroops() < 2)
            throw new Exception("Attacking Country must have at least 2 troops");

        Integer[] attackerRolls = Dice.rollMultipleTimes((Integer) attackingCountry.getNumberOfTroops());
        Integer[] defenderRolls = Dice.rollMultipleTimes((Integer) defendingCountry.getNumberOfTroops());

        for(int i = 0; i < defenderRolls.length; i++)
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
