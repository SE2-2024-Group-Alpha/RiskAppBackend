package se2.alpha.riskappbackend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import se2.alpha.riskappbackend.model.db.Player;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskCardType;
import se2.alpha.riskappbackend.model.db.TradeType;

public class PlayerTest {
    Player player;
    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testCanPlayerTradeRiskCardsWithNoRiskCards() throws Exception {
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
    }
    @Test
    void testCanPlayerTradeRiskCardsArtillery() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.ARTILLERY, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsInfantry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.INFANTRY, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsCavalry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.CAVALRY, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsArtilleryJoker() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.ARTILLERY_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsInfantryJoker() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.INFANTRY_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsCavalryJoker() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.CAVALRY_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsMixed() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.MIXED, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsMixedJokerCavalry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.MIXED_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsMixedJokerInfantry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.MIXED_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testCanPlayerTradeRiskCardsMixedJokerArtillery() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        assertEquals(TradeType.NONE, player.canTradeRiskCards());
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        assertEquals(TradeType.MIXED_JOKER, player.canTradeRiskCards());
    }

    @Test
    void testTradeRiskCardNotPossible() {
        Exception ex = assertThrows(Exception.class, () -> {player.tradeRiskCards();});
        assertEquals("Player cannot trade any risk cards", ex.getMessage());
    }

    @Test
    void testTradeThreeArtillery() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }
    @Test
    void testTradeThreeInfantry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }
    @Test
    void testTradeThreeCavalry() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }
    @Test
    void testTradeMixed() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.INFANTRY, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }
    @Test
    void testTradeWithOtherCards() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.ARTILLERY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.tradeRiskCards();
        assertEquals(1, player.getCards().size());
        assertEquals(RiskCardType.ARTILLERY, player.getCards().get(0).getType());
    }
    @Test
    void testTradeWithOneJoker() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }
    @Test
    void testTradeWithTwoJokers() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.CAVALRY, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }

    @Test
    void testTradeWithThreeJokers() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(0, player.getCards().size());
    }

    @Test
    void testNewTroopsForFirstTrade() throws Exception {
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(4, player.getArmy().size());
    }

    @Test
    void testNewTroopsForSecondTrade() throws Exception {
        player.setCntRiskCardsTraded(1);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(6, player.getArmy().size());
    }
    @Test
    void testNewTroopsForThirdTrade() throws Exception {
        player.setCntRiskCardsTraded(2);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(8, player.getArmy().size());
    }
    @Test
    void testNewTroopsForFourthTrade() throws Exception {
        player.setCntRiskCardsTraded(3);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(10, player.getArmy().size());
    }
    @Test
    void testNewTroopsForFifthTrade() throws Exception {
        player.setCntRiskCardsTraded(4);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(12, player.getArmy().size());
    }
    @Test
    void testNewTroopsForSixthTrade() throws Exception {
        player.setCntRiskCardsTraded(5);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(15, player.getArmy().size());
    }
    @Test
    void testNewTroopsForSeventhTrade() throws Exception {
        player.setCntRiskCardsTraded(6);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(20, player.getArmy().size());
    }
    @Test
    void testNewTroopsForEightTrade() throws Exception {
        player.setCntRiskCardsTraded(7);
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.addRiskCard(new RiskCard(RiskCardType.JOKER, null));
        player.tradeRiskCards();
        assertEquals(25, player.getArmy().size());
    }
}
