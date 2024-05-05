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

    public RiskCard getNewRiskCard(String id) throws Exception
    {
        Player player = getPlayerById(id);
        RiskCard riskCard = board.getNewRiskCard();
        player.addRiskCard(riskCard);
        return riskCard;
    }

    public ArrayList<RiskCard> getRiskCardsByPlayer(String id) throws Exception
    {
        for(Player player : players)
        {
            if(player.getId().equals(id))
                return player.getCards();
        }
        throw new Exception("no player with this id found");
    }

    public boolean canPlayerTradeRiskCards(String id) throws Exception
    {
        Player player = getPlayerById(id);
        return !player.canTradeRiskCards().equals(TradeType.NONE);
    }

    public void tradeRiskCards(String id) throws Exception
    {
        Player player = getPlayerById(id);
        TradeType tradeType = player.canTradeRiskCards();
        if(tradeType.equals(TradeType.NONE))
            throw new Exception("Player cannot trade any risk cards");
        player.tradeRiskCards();
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

    public void attack(Player attacker, Player defender, Country attackingCountry, Country defendingCountry) throws Exception
    {
        int attackerLosses = 0;
        int defenderLosses = 0;
        if(!attackingCountry.getAttackableCountries().contains(defendingCountry))
            throw new Exception("Cannot attack this country");

        if(attackingCountry.getArmy().size() < 2)
            throw new Exception("Attacking Country must have at least 2 troops");

        int[] attackerRolls = Dice.rollMultipleTimes(attackingCountry.getArmy().size());
        int[] defenderRolls = Dice.rollMultipleTimes(defendingCountry.getArmy().size());

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

    private boolean processDefender(Player player, Country country, int losses)
    {
        processLosses(player, country, losses);

        if(country.getArmy().isEmpty()) {
            country.setOwner(null);
            if(country.getContinent().getOwner().equals(player))
                country.getContinent().setOwner(null);
        }

        return country.getArmy().isEmpty();
    }

    private static void processLosses(Player player, Country country, int losses) {
        ArrayList<Troop> troops = country.getArmy();
        for(int i = 0; i < losses; i ++)
        {
            country.removeArmy(troops.get(i));
            player.removeArmy(troops.get(i));
        }
    }

    private void processAttacker(Player player, Country attackingCountry, Country defendingCountry, int losses, boolean attackSuccessful)
    {
        processLosses(player, attackingCountry, losses);
        if(attackSuccessful)
        {
            if(attackingCountry.getArmy().size() > 1) {
                Troop t = attackingCountry.getArmy().get(0);
                attackingCountry.removeArmy(t);
                defendingCountry.addArmy(t);
                defendingCountry.setOwner(player);
            }

            if(isContinentOwnedByPlayer(defendingCountry.getContinent(), player))
                defendingCountry.getContinent().setOwner(player);
        }
    }
    private boolean isContinentOwnedByPlayer(Continent continent, Player player)
    {
        for(Country country : continent.getCountries())
        {
            if (!country.getOwner().equals(player)) {
                return false;
            }
        }
        return true;
    }
}
