package se2.alpha.riskappbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.Player;
import se2.alpha.riskappbackend.model.db.RiskController;
import se2.alpha.riskappbackend.util.GameSetupFactory;

public class GameSetupFactoryTest {
    @Test
    void testThreePlayerGame() throws Exception {
        RiskController riskController = GameSetupFactory.setupThreePlayerGame("1", "2", "3");
        assertEquals(3, riskController.getPlayers().size());
    }

    @Test
    void testFourPlayerGame() throws Exception {
        RiskController riskController = GameSetupFactory.setupFourPlayerGame("1", "2", "3", "4");
        assertEquals(4, riskController.getPlayers().size());
    }

    @Test
    void testFivePlayerGame() throws Exception {
        RiskController riskController = GameSetupFactory.setupFivePlayerGame("1", "2", "3", "4", "5");
        assertEquals(5, riskController.getPlayers().size());
    }

    @Test
    void testSixPlayerGame() throws Exception {
        RiskController riskController = GameSetupFactory.setupSixPlayerGame("1", "2", "3", "4", "5", "6");
        assertEquals(6, riskController.getPlayers().size());
    }

    @Test
    void testContinents() throws Exception {
        RiskController riskController = GameSetupFactory.setupSixPlayerGame("1", "2", "3", "4", "5", "6");
        assertEquals(6, riskController.getBoard().getContinents().size());
    }

    @Test
    void testCountries() throws Exception {
        int nrCountries = 0;
        RiskController riskController = GameSetupFactory.setupSixPlayerGame("1", "2", "3", "4", "5", "6");
        for(Continent continent : riskController.getBoard().getContinents())
            nrCountries += continent.getCountries().size();
        assertEquals(42, nrCountries);
    }
}
