package se.alpha.riskappbackend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import se.alpha.riskappbackend.model.db.Board;
import se.alpha.riskappbackend.model.db.Continent;
import se.alpha.riskappbackend.model.db.Country;
import se.alpha.riskappbackend.model.db.Dice;
import se.alpha.riskappbackend.model.db.Player;
import se.alpha.riskappbackend.model.db.RiskCard;
import se.alpha.riskappbackend.model.db.RiskController;
import se.alpha.riskappbackend.model.exception.RiskException;
import se.alpha.riskappbackend.util.GameSetupFactory;

import static org.junit.jupiter.api.Assertions.*;

class RiskControllerTest {
    RiskController riskController;
    MockedStatic<Dice> mockedStatic;
    final int PLAYERNUMBEROFTROOPS = 35;

    @BeforeEach
    void setUp() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        riskController = GameSetupFactory.setupThreePlayerGame(players);
        mockedStatic = Mockito.mockStatic(Dice.class);
    }
    @AfterEach
    void tearDown() {
        mockedStatic.close();
        Mockito.reset();
    }

    @Test
    void testAttackCompleteSuccess() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 10)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(20);
        defendingCountry.addArmy(1);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertEquals(attacker, defendingCountry.getOwner());
    }

    @Test
    void testAttackCompleteFail() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{1, 1, 1, 1, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1}); // one is needed because the attack goes into second round with losses
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(3);
        defendingCountry.addArmy(15);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertEquals(defender, defendingCountry.getOwner());
    }
    @Test
    void testAttackWithOneLossOnBothSides() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 2)).thenReturn(new Integer[]{6, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 10)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1}); //needed for second round of attack
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 9)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6}); //needed for second round of attack
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(20);
        defendingCountry.addArmy(2);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertEquals(attacker, defendingCountry.getOwner());
    }
    @Test
    void testAttackWithTwoLossesOnBothSides() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{6, 6, 1, 1, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{5, 5, 5, 5});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 2)).thenReturn(new Integer[]{6, 6}); //needed for second round of attack
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 3)).thenReturn(new Integer[]{1, 1, 1}); //needed for second and third round of attack
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1}); //needed for third round of attack
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(6);
        defendingCountry.addArmy(20);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertEquals(defender, defendingCountry.getOwner());
    }
    @Test
    void testAttackCaptureContinent() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{6, 6, 6, 6, 6});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{1, 1, 1, 1});
        Board board = riskController.getBoard();
        Continent continent = board.getContinents().get(0);
        Country attackingCountry = continent.getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(1);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        for(Country country : continent.getCountries())
        {
            attacker.controlCountry(country);
        }
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(20);
        defendingCountry.addArmy(4);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertEquals(attacker, continent.getOwner());
    }
    @Test
    void testAttackLoseContinent() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{6, 6, 6, 6, 6});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{1, 1, 1, 1});
        Board board = riskController.getBoard();
        Continent continent = board.getContinents().get(0);
        Country attackingCountry = board.getContinents().get(4).getCountries().get(2);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(5);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attackingCountry.setOwner(attacker);
        continent.setOwner(defender);
        for(Country country : continent.getCountries())
        {
            country.setOwner(defender);
        }
        defendingCountry.setOwner(defender);
        attackingCountry.addArmy(20);
        defendingCountry.addArmy(4);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertNull(continent.getOwner());
    }
    @Test
    void testAttackSuccessfulPlayerEliminated() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 10)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(11);
        defendingCountry.addArmy(1);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertTrue(defender.isEliminated());
    }
    @Test
    void testAttackSuccessfulPlayerNotEliminated() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 1)).thenReturn(new Integer[]{1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 10)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        defender.controlCountry(attackingCountry.getAttackableCountries().get(1));
        attackingCountry.addArmy(11);
        defendingCountry.addArmy(1);
        riskController.attack(attacker.getId(), defender.getId(), attackingCountry.getName(), defendingCountry.getName());
        assertFalse(defender.isEliminated());
    }
    @Test
    void testMoveTroops() throws Exception {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = moveFromCountry.getAttackableCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        player.controlCountry(moveFromCountry);
        player.controlCountry(moveToCountry);
        moveFromCountry.addArmy(10);
        moveToCountry.addArmy(10);
        int cntArmyMoveFromCountry = moveFromCountry.getNumberOfTroops();
        int cntArmyMoveToCountry = moveToCountry.getNumberOfTroops();
        riskController.moveTroops(player.getId(), moveFromCountry.getName(), moveToCountry.getName(), 5);
        assertEquals(cntArmyMoveFromCountry - 5, moveFromCountry.getNumberOfTroops());
        assertEquals(cntArmyMoveToCountry + 5, moveToCountry.getNumberOfTroops());
    }
    @Test
    void testMoveTroopsNotEnoughTroops() {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = moveFromCountry.getAttackableCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        player.controlCountry(moveFromCountry);
        player.controlCountry(moveToCountry);
        moveFromCountry.addArmy(1);
        moveToCountry.addArmy(10);

        RiskException exception = assertThrows(RiskException.class, () -> riskController.moveTroops(player.getId(), moveFromCountry.getName(), moveToCountry.getName(), 10));
        assertEquals("not enough troops in this country to move from", exception.getMessage());
    }
    @Test
    void testMoveTroopsCountriesNotControlledByPlayer() {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = moveFromCountry.getAttackableCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        moveFromCountry.addArmy(10);
        moveToCountry.addArmy(10);

        RiskException exception = assertThrows(RiskException.class, () -> riskController.moveTroops(player.getId(), moveFromCountry.getName(), moveToCountry.getName(), 5));
        assertEquals("countries must be owned by player", exception.getMessage());
    }
    @Test
    void testMoveTroopsNotAllowed() {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = board.getContinents().get(1).getCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        player.controlCountry(moveFromCountry);
        player.controlCountry(moveToCountry);
        moveFromCountry.addArmy(10);
        moveToCountry.addArmy(10);

        RiskException exception = assertThrows(RiskException.class, () -> riskController.moveTroops(player.getId(), moveFromCountry.getName(), moveToCountry.getName(), 5));
        assertEquals("moving troops between those 2 countries not allowed", exception.getMessage());
    }
    @Test
    void testGetNewTroopsFewerThanNineTerritories() throws Exception {
        Player player = riskController.getPlayers().get(0);
        riskController.getNewTroops(player.getId());
        assertEquals(PLAYERNUMBEROFTROOPS + 3, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS + 3, player.getTotalNumberOfTroops());
    }
    @Test
    void testGetNewTroopsOwningAsia() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Board board = riskController.getBoard();
        Continent continent = board.getContinents().get(0);
        for(Country country : continent.getCountries())
            player.controlCountry(country);
        player.controlContinent(continent);
        riskController.getNewTroops(player.getId());
        assertEquals(PLAYERNUMBEROFTROOPS + 9, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS + 9, player.getTotalNumberOfTroops());
    }

    @Test
    void testGetNewTroopsOwningAsiaAndNorthAmerica() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Board board = riskController.getBoard();
        Continent continent = board.getContinents().get(0);
        for(Country country : continent.getCountries())
            player.controlCountry(country);
        player.controlContinent(continent);
        Continent continent2 = board.getContinents().get(1);
        for(Country country : continent2.getCountries())
            player.controlCountry(country);
        player.controlContinent(continent2);
        riskController.getNewTroops(player.getId());
        assertEquals(PLAYERNUMBEROFTROOPS + 17, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS + 17, player.getTotalNumberOfTroops());
    }

    @Test
    void testStrengthenCountry() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Country country = riskController.getBoard().getContinents().get(0).getCountries().get(0);
        player.controlCountry(country);
        riskController.strengthenCountry(player.getId(), country.getName(), 5);
        assertEquals(PLAYERNUMBEROFTROOPS - 5, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS, player.getTotalNumberOfTroops());
        assertEquals(5, country.getNumberOfTroops());
    }

    @Test
    void testSeizeCountry() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Country country = null;
        for(Continent continent : riskController.getBoard().getContinents())
        {
            for(Country currentCountry : continent.getCountries())
            {
                if(currentCountry.getOwner() == null)
                {
                    country = currentCountry;
                    break;
                }
            }
        }
        riskController.seizeCountry(player.getId(), country.getName(), 5);
        assertEquals(player, country.getOwner());
        assertEquals(PLAYERNUMBEROFTROOPS - 5, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS, player.getTotalNumberOfTroops());
        assertEquals(5, country.getNumberOfTroops());
    }

    @Test
    void testSeizeCountryNotAllowed() {
        Player player = riskController.getPlayers().get(0);
        Country country = riskController.getBoard().getContinents().get(0).getCountries().get(0);
        riskController.getPlayers().get(1).controlCountry(country);
        RiskException exception = assertThrows(RiskException.class, () -> riskController.seizeCountry(player.getId(), country.getName(), 5));
        assertEquals("Country cannot be seized while being owned by another player", exception.getMessage());
    }

    @Test
    void testGetNewRiskCard() throws Exception {
        Player player = riskController.getPlayers().get(0);
        riskController.getNewRiskCard(player.getId());
        assertEquals(1, player.getCards().size());
        riskController.getNewRiskCard(player.getId());
        assertEquals(2, player.getCards().size());
    }

    @Test
    void testGetNewRiskCardNewStack() throws Exception {
        Player player = riskController.getPlayers().get(0);
        for(int i = 0; i < 100; i++)
        {
            riskController.getNewRiskCard(player.getId());
        }
        assertFalse(riskController.getBoard().getCards().isEmpty());
    }

    @Test
    void testGetRiskCardsByPlayer() throws Exception {
        Player player = riskController.getPlayers().get(0);
        riskController.getNewRiskCard(player.getId());
        riskController.getNewRiskCard(player.getId());
        List<RiskCard> riskCards = riskController.getRiskCardsByPlayer(player.getId());
        assertEquals(2, riskCards.size());
    }

    @Test
    void testIsFirstPlayerActive() {
        Player player1 = riskController.getPlayers().get(0);
        Player player2 = riskController.getPlayers().get(1);
        Player player3 = riskController.getPlayers().get(2);
        assertTrue(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
    }

    @Test
    void testPlayerActiveAfterOneTurn() {
        Player player1 = riskController.getPlayers().get(0);
        Player player2 = riskController.getPlayers().get(1);
        Player player3 = riskController.getPlayers().get(2);
        assertTrue(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
        riskController.endPlayerTurn();
        assertFalse(player1.isCurrentTurn());
        assertTrue(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
    }

    @Test
    void testMultipleTurns() {
        Player player1 = riskController.getPlayers().get(0);
        Player player2 = riskController.getPlayers().get(1);
        Player player3 = riskController.getPlayers().get(2);
        assertTrue(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
        riskController.endPlayerTurn();
        assertFalse(player1.isCurrentTurn());
        assertTrue(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
        riskController.endPlayerTurn();
        assertFalse(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertTrue(player3.isCurrentTurn());
        riskController.endPlayerTurn();
        assertTrue(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
    }

    @Test
    void testCurrentTurnWithEliminatedPlayers() {
        Player player1 = riskController.getPlayers().get(0);
        Player player2 = riskController.getPlayers().get(1);
        Player player3 = riskController.getPlayers().get(2);
        player2.setEliminated(true);
        assertTrue(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertFalse(player3.isCurrentTurn());
        riskController.endPlayerTurn();
        assertFalse(player1.isCurrentTurn());
        assertFalse(player2.isCurrentTurn());
        assertTrue(player3.isCurrentTurn());
    }

    @Test
    void testGetRiskCardsByPlayerWithoutValidPlayer() {
        RiskException exception = assertThrows(RiskException.class, () -> riskController.getRiskCardsByPlayer(""));
        assertEquals("no player with this id found", exception.getMessage());
        assertEquals("custom", exception.getType());
    }

    @Test
    void testMoveTroopsWithoutValidCountry() {
        RiskException exception = assertThrows(RiskException.class, () -> riskController.moveTroops(riskController.getPlayers().get(0).getId(), "", "", 1));
        assertEquals("no country with this name found", exception.getMessage());
        assertEquals("custom", exception.getType());
    }

    @Test
    void testCanPlayerTradeRiskCards() throws RiskException {
        assertFalse(riskController.canPlayerTradeRiskCards(riskController.getPlayers().get(0).getId()));
    }

    @Test
    void testTradeRiskCardsNotAllowed() {
        RiskException exception = assertThrows(RiskException.class, () -> riskController.tradeRiskCards(riskController.getPlayers().get(0).getId()));
        assertEquals("Player cannot trade any risk cards", exception.getMessage());
        assertEquals("custom", exception.getType());
    }

    @Test
    void testGetActivePlayer() {
        Player player = riskController.getActivePlayer();
        assertTrue(player.isCurrentTurn());
        riskController.endPlayerTurn();
        assertFalse(player.isCurrentTurn());
        assertNotEquals(player, riskController.getActivePlayer());
    }

    @Test
    void testIsPlayerEliminated() {
        Player player = riskController.getActivePlayer();
        assertFalse(player.isEliminated());
    }

    @Test
    void testHasPlayerWon() throws RiskException {
        ArrayList<Player> players = (ArrayList<Player>) riskController.getPlayers();
        for(Player player : players)
            player.setEliminated(true);
        Player winner = players.get(0);
        winner.setEliminated(false);
        assertTrue(riskController.hasPlayerWon(winner.getId()));
    }
}
