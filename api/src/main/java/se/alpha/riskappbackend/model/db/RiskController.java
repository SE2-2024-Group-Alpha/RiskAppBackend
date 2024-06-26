package se.alpha.riskappbackend.model.db;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import se.alpha.riskappbackend.model.exception.RiskException;

@Getter
public class RiskController {
    private final UUID id;
    private final List<Player> players;
    private final Board board;
    private int idxPlayerTurn;
    private static final String CUSTOM_EXCEPTION_TYPE = "custom";

    public RiskController(List<Player> players, Board board) {
        this.id = UUID.randomUUID();
        this.players = players;
        this.board = board;
        idxPlayerTurn = 0;
        players.get(idxPlayerTurn).setCurrentTurn(true);
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

    public RiskCard getNewRiskCard(String playerId) throws RiskException
    {
        Player player = getPlayerById(playerId);
        RiskCard riskCard = board.getNewRiskCard();
        player.addRiskCard(riskCard);
        return riskCard;
    }

    public List<RiskCard> getRiskCardsByPlayer(String playerId) throws RiskException
    {
        for(Player player : players)
        {
            if(player.getId().equals(playerId))
                return player.getCards();
        }
        throw new RiskException(CUSTOM_EXCEPTION_TYPE, "no player with this id found");
    }

    public boolean canPlayerTradeRiskCards(String playerId) throws RiskException
    {
        Player player = getPlayerById(playerId);
        return !player.canTradeRiskCards().equals(TradeType.NONE);
    }

    public void tradeRiskCards(String playerId) throws RiskException
    {
        Player player = getPlayerById(playerId);
        TradeType tradeType = player.canTradeRiskCards();
        if(tradeType.equals(TradeType.NONE))
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Player cannot trade any risk cards");
        player.tradeRiskCards();
    }

    public void moveTroops(String playerId, String moveFromCountryName, String moveToCountryName, int cntTroops) throws RiskException {
        Player player = getPlayerById(playerId);
        Country moveFromCountry = getCountryByName(moveFromCountryName);
        Country moveToCountry = getCountryByName(moveToCountryName);
        if(!player.equals(moveFromCountry.getOwner()) || !player.equals(moveToCountry.getOwner()))
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "countries must be owned by player");
        if(moveFromCountry.getNumberOfTroops() <= cntTroops)
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "not enough troops in this country to move from");
        if(!moveFromCountry.getAttackableCountries().contains(moveToCountry))
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "moving troops between those 2 countries not allowed");

        moveFromCountry.removeArmy(cntTroops);
        moveToCountry.addArmy(cntTroops);
    }

    public int strengthenCountry(String playerId, String countryName, int cntTroops) throws RiskException
    {
        Player player = getPlayerById(playerId);
        Country country = getCountryByName(countryName);
        if(!player.equals(country.getOwner()))
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Country not owned by player");

        player.strengthenCountry(country, cntTroops);
        return country.getNumberOfTroops();
    }

    public void seizeCountry(String playerId, String countryName, int cntTroops) throws RiskException
    {
        Player player = getPlayerById(playerId);
        Country country = getCountryByName(countryName);
        if(country.getOwner() != null)
            throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Country cannot be seized while being owned by another player");

        player.seizeCountry(country, cntTroops);
    }

    public void getNewTroops(String playerId) throws RiskException
    {
        int cntNewTroops = 0;
        Player player = getPlayerById(playerId);
        cntNewTroops = player.getControlledCountries().size() / 3;
        cntNewTroops += player.getControlledContinents().size() * 5;

        if(cntNewTroops < 3)
            cntNewTroops = 3;

        player.addArmy(cntNewTroops);
    }

    public void attack(String attackerPlayerId, String defenderPlayerId, String attackingCountryName, String defendingCountryName) throws RiskException
    {
        Dice dice = new Dice();
        int attackerLosses = 0;
        int defenderLosses = 0;
        System.out.println("attacker: " + attackerPlayerId);
        System.out.println("defender: " + defenderPlayerId);
        Player attacker = getPlayerById(attackerPlayerId);
        Country attackingCountry = getCountryByName(attackingCountryName);
        Country defendingCountry = getCountryByName(defendingCountryName);
        if(defenderPlayerId == null) {
            seizeCountry(attackerPlayerId, defendingCountryName, attackingCountry.getNumberOfTroops() - 1);
            attackingCountry.removeArmy(attackingCountry.getNumberOfTroops() - 1);
        }
        else {
            Player defender = getPlayerById(defenderPlayerId);

            if (!attackingCountry.getAttackableCountries().contains(defendingCountry))
                throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Cannot attack this country");

            if (attackingCountry.getNumberOfTroops() < 2)
                throw new RiskException(CUSTOM_EXCEPTION_TYPE, "Attacking Country must have at least 2 troops");

            int[] attackerRolls = dice.rollMultiple((attackingCountry.getNumberOfTroops() - 1));
            int[] defenderRolls = dice.rollMultiple(defendingCountry.getNumberOfTroops());

            for (int i = 0; i < Math.min(defenderRolls.length, attackerRolls.length); i++) {
                if (attackerRolls[i] > defenderRolls[i]) {
                    defenderLosses++;
                } else {
                    attackerLosses++;
                }
            }

            boolean attackSuccessful = processDefender(defender, defendingCountry, defenderLosses);
            processAttacker(attacker, attackingCountry, defendingCountry, attackerLosses, attackSuccessful);

            if (!attackSuccessful && attackingCountry.getNumberOfTroops() > 1)
                attack(attackerPlayerId, defenderPlayerId, attackingCountryName, defendingCountryName);
        }
    }

    public Player getActivePlayer()
    {
        for(Player player : players)
            if(player.isCurrentTurn())
                return player;
        return null;
    }

    public boolean isPlayerEliminated(String playerId) throws RiskException
    {
        Player player = getPlayerById(playerId);
        return player.isEliminated();
    }

    public boolean hasPlayerWon(String playerId) throws RiskException
    {
        Player playerToCheck = getPlayerById(playerId);
        if(playerToCheck.isEliminated())
            return false;
        for(Player player : players)
        {
            if(!player.isEliminated() && !player.getId().equals(playerId))
                return false;
        }
        return true;
    }

    private Player getPlayerById(String id) throws RiskException
    {
        for(Player player : players)
        {
            if(player.getId().equals(id))
                return player;
        }
        for(Player player : players)
        {
            if(player.getName().equals(id))
                return player;
        }
        throw new RiskException(CUSTOM_EXCEPTION_TYPE, "no player with this id found");
    }

    public Country getCountryByName(String countryName) throws RiskException
    {
        for(Continent continent : board.getContinents())
        {
            for(Country country : continent.getCountries())
                if(countryName.equals(country.getName()))
                    return country;
        }
        throw new RiskException(CUSTOM_EXCEPTION_TYPE, "no country with this name found");
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
