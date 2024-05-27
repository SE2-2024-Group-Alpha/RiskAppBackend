package se.alpha.riskappbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;

import se.alpha.riskappbackend.model.db.Continent;
import se.alpha.riskappbackend.model.db.Player;
import se.alpha.riskappbackend.model.db.RiskController;
import se.alpha.riskappbackend.util.GameSetupFactory;

public class GameSetupFactoryTest {
    @Test
    void testThreePlayerGame() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        RiskController riskController = GameSetupFactory.setupThreePlayerGame(players);
        assertEquals(3, riskController.getPlayers().size());
    }

    @Test
    void testFourPlayerGame() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        players.add(new Player("4", "", Color.GREEN.getRGB()));
        RiskController riskController = GameSetupFactory.setupFourPlayerGame(players);
        assertEquals(4, riskController.getPlayers().size());
    }

    @Test
    void testFivePlayerGame() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        players.add(new Player("4", "", Color.GREEN.getRGB()));
        players.add(new Player("5", "", Color.ORANGE.getRGB()));
        RiskController riskController = GameSetupFactory.setupFivePlayerGame(players);
        assertEquals(5, riskController.getPlayers().size());
    }

    @Test
    void testSixPlayerGame() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        players.add(new Player("4", "", Color.GREEN.getRGB()));
        players.add(new Player("5", "", Color.ORANGE.getRGB()));
        players.add(new Player("6", "", Color.PINK.getRGB()));
        RiskController riskController = GameSetupFactory.setupSixPlayerGame(players);
        assertEquals(6, riskController.getPlayers().size());
    }

    @Test
    void testContinents() throws Exception {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        players.add(new Player("4", "", Color.GREEN.getRGB()));
        players.add(new Player("5", "", Color.ORANGE.getRGB()));
        players.add(new Player("6", "", Color.PINK.getRGB()));
        RiskController riskController = GameSetupFactory.setupSixPlayerGame(players);
        assertEquals(6, riskController.getBoard().getContinents().size());
    }

    @Test
    void testCountries() throws Exception {
        int nrCountries = 0;
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("1", "", Color.BLUE.getRGB()));
        players.add(new Player("2", "", Color.RED.getRGB()));
        players.add(new Player("3", "", Color.YELLOW.getRGB()));
        players.add(new Player("4", "", Color.GREEN.getRGB()));
        players.add(new Player("5", "", Color.ORANGE.getRGB()));
        players.add(new Player("6", "", Color.PINK.getRGB()));
        RiskController riskController = GameSetupFactory.setupSixPlayerGame(players);
        for(Continent continent : riskController.getBoard().getContinents())
            nrCountries += continent.getCountries().size();
        assertEquals(42, nrCountries);
    }
}
