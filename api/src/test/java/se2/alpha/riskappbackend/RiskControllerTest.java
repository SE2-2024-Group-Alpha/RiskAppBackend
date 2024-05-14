package se2.alpha.riskappbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import se2.alpha.riskappbackend.model.db.Board;
import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.Dice;
import se2.alpha.riskappbackend.model.db.Player;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskController;
import se2.alpha.riskappbackend.util.GameSetupFactory;

public class RiskControllerTest {
    RiskController riskController;
    MockedStatic<Dice> mockedStatic;
    final int PLAYERNUMBEROFTROOPS = 35;

    @BeforeEach
    void setUp() throws Exception {
        riskController = GameSetupFactory.setupThreePlayerGame("1", "2", "3");
        mockedStatic = Mockito.mockStatic(Dice.class);
    }
    @AfterEach
    void tearDown() throws Exception {
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
        attackingCountry.addArmy(10);
        defendingCountry.addArmy(1);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
        assertEquals(attacker, defendingCountry.getOwner());
        assertEquals(9, defendingCountry.getNumberOfTroops());
        assertEquals(1, attackingCountry.getNumberOfTroops());
    }

    @Test
    void testAttackCompleteFail() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{1, 1, 1, 1, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(5);
        defendingCountry.addArmy(4);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
        assertEquals(defender, defendingCountry.getOwner());
        assertEquals(4, defendingCountry.getNumberOfTroops());
        assertEquals(1, attackingCountry.getNumberOfTroops());
    }
    @Test
    void testAttackWithOneLossOnBothSides() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 2)).thenReturn(new Integer[]{6, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 10)).thenReturn(new Integer[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(10);
        defendingCountry.addArmy(2);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
        assertEquals(defender, defendingCountry.getOwner());
        assertEquals(1, defendingCountry.getNumberOfTroops());
        assertEquals(9, attackingCountry.getNumberOfTroops());
    }
    @Test
    void testAttackWithTwoLossesOnBothSides() throws Exception {
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 5)).thenReturn(new Integer[]{6, 6, 1, 1, 1});
        mockedStatic.when(() -> Dice.rollMultipleTimes((Integer) 4)).thenReturn(new Integer[]{5, 5, 5, 5});
        Board board = riskController.getBoard();
        Country attackingCountry = board.getContinents().get(0).getCountries().get(0);
        Country defendingCountry = attackingCountry.getAttackableCountries().get(0);
        Player attacker = riskController.getPlayers().get(0);
        Player defender = riskController.getPlayers().get(1);
        attacker.controlCountry(attackingCountry);
        defender.controlCountry(defendingCountry);
        attackingCountry.addArmy(5);
        defendingCountry.addArmy(4);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
        assertEquals(defender, defendingCountry.getOwner());
        assertEquals(2, defendingCountry.getNumberOfTroops());
        assertEquals(3, attackingCountry.getNumberOfTroops());
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
        attackingCountry.addArmy(5);
        defendingCountry.addArmy(4);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
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
        attackingCountry.addArmy(5);
        defendingCountry.addArmy(4);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
        assertEquals(null, continent.getOwner());
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
        attackingCountry.addArmy(10);
        defendingCountry.addArmy(1);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
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
        attackingCountry.addArmy(10);
        defendingCountry.addArmy(1);
        riskController.attack(attacker, defender, attackingCountry, defendingCountry);
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
        riskController.moveTroops(player.getId(), moveFromCountry, moveToCountry, 5);
        assertEquals(5, moveFromCountry.getNumberOfTroops());
        assertEquals(15, moveToCountry.getNumberOfTroops());
    }
    @Test
    void testMoveTroopsNotEnoughTroops() throws Exception {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = moveFromCountry.getAttackableCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        player.controlCountry(moveFromCountry);
        player.controlCountry(moveToCountry);
        moveFromCountry.addArmy(3);
        moveToCountry.addArmy(10);

        Exception exception = assertThrows(Exception.class, () -> riskController.moveTroops(player.getId(), moveFromCountry, moveToCountry, 5));
        assertEquals("not enough troops in this country to move from", exception.getMessage());
    }
    @Test
    void testMoveTroopsCountriesNotControlledByPlayer() throws Exception {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = moveFromCountry.getAttackableCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        moveFromCountry.addArmy(10);
        moveToCountry.addArmy(10);

        Exception exception = assertThrows(Exception.class, () -> riskController.moveTroops(player.getId(), moveFromCountry, moveToCountry, 5));
        assertEquals("countries must be owned by player", exception.getMessage());
    }
    @Test
    void testMoveTroopsNotAllowed() throws Exception {
        Board board = riskController.getBoard();
        Country moveFromCountry = board.getContinents().get(0).getCountries().get(0);
        Country moveToCountry = board.getContinents().get(1).getCountries().get(0);
        Player player = riskController.getPlayers().get(0);
        player.controlCountry(moveFromCountry);
        player.controlCountry(moveToCountry);
        moveFromCountry.addArmy(10);
        moveToCountry.addArmy(10);

        Exception exception = assertThrows(Exception.class, () -> riskController.moveTroops(player.getId(), moveFromCountry, moveToCountry, 5));
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
        riskController.strengthenCountry(player.getId(), country, 5);
        assertEquals(PLAYERNUMBEROFTROOPS - 5, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS, player.getTotalNumberOfTroops());
        assertEquals(5, country.getNumberOfTroops());
    }

    @Test
    void testSeizeCountry() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Country country = riskController.getBoard().getContinents().get(0).getCountries().get(0);
        riskController.seizeCountry(player.getId(), country, 5);
        assertEquals(player, country.getOwner());
        assertEquals(PLAYERNUMBEROFTROOPS - 5, player.getFreeNumberOfTroops());
        assertEquals(PLAYERNUMBEROFTROOPS, player.getTotalNumberOfTroops());
        assertEquals(5, country.getNumberOfTroops());
    }

    @Test
    void testSeizeCountryNotAllowed() throws Exception {
        Player player = riskController.getPlayers().get(0);
        Country country = riskController.getBoard().getContinents().get(0).getCountries().get(0);
        riskController.getPlayers().get(1).controlCountry(country);
        Exception exception = assertThrows(Exception.class, () -> riskController.seizeCountry(player.getId(), country, 5));
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
    void testGetRiskCardsByPlayer() throws Exception {
        Player player = riskController.getPlayers().get(0);
        riskController.getNewRiskCard(player.getId());
        riskController.getNewRiskCard(player.getId());
        ArrayList<RiskCard> riskCards = riskController.getRiskCardsByPlayer(player.getId());
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
}
