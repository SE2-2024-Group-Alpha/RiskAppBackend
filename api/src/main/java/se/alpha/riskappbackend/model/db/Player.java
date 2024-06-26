package se.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.Iterator;

import lombok.Getter;
import se.alpha.riskappbackend.model.exception.RiskException;

@Getter
public class Player {
    private String id;
    private String name;
    private int color;
    private transient final ArrayList<RiskCard> cards;
    private boolean eliminated;
    private int cntRiskCardsTraded;
    private int totalNumberOfTroops;
    private int freeNumberOfTroops;
    private boolean currentTurn;
    private transient final ArrayList<Country> controlledCountries;
    private transient final ArrayList<Continent> controlledContinents;
    private static final int TROOPSFORFIRSTTRADE = 4;
    private static final int TROOPSFORSECONDTRADE = 6;
    private static final int TROOPSFORTHIRDTRADE = 8;
    private static final int TROOPSFORFOURTHTRADE = 10;
    private static final int TROOPSFORFIFTHTRADE = 12;
    private static final int TROOPSFORSIXTHTRADE = 15;

    public Player(String id, String name, int color, int numberOfTroops) {
        this.id = id;
        this.name = name;
        this.color = color;
        cards = new ArrayList<>();
        controlledContinents = new ArrayList<>();
        controlledCountries = new ArrayList<>();
        freeNumberOfTroops = numberOfTroops;
        totalNumberOfTroops = numberOfTroops;
        currentTurn = false;
    }

    public Player(String id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
        cards = new ArrayList<>();
        controlledContinents = new ArrayList<>();
        controlledCountries = new ArrayList<>();
        freeNumberOfTroops = 0;
        totalNumberOfTroops = 0;
        currentTurn = false;
    }

    public Player() {
        cards = new ArrayList<>();
        controlledContinents = new ArrayList<>();
        controlledCountries = new ArrayList<>();
        currentTurn = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCntRiskCardsTraded(int cntRiskCardsTraded)
    {
        this.cntRiskCardsTraded = cntRiskCardsTraded;
    }

    public void addArmy(int numberOfTroops)
    {
        freeNumberOfTroops += numberOfTroops;
        totalNumberOfTroops += numberOfTroops;
    }

    public void removeArmy(int numberOfTroops)
    {
        totalNumberOfTroops -= numberOfTroops;
    }

    public void addRiskCard(RiskCard card)
    {
        cards.add(card);
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void setCurrentTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void controlCountry(Country c)
    {
        controlledCountries.add(c);
        c.setOwner(this);
    }

    public void loseControlOverCountry(Country c)
    {
        controlledCountries.remove(c);
        c.setOwner(null);
        if(controlledCountries.isEmpty())
            eliminated = true;
    }

    public void controlContinent(Continent c)
    {
        controlledContinents.add(c);
        c.setOwner(this);
    }

    public void loseControlOverContinent(Continent c)
    {
        controlledContinents.remove(c);
        c.setOwner(null);
    }

    public void strengthenCountry(Country country, int cntTroops)
    {
        country.addArmy(cntTroops);
        this.freeNumberOfTroops -= cntTroops;
    }

    public void seizeCountry(Country country, int cntTroops)
    {
        controlCountry(country);
        country.addArmy(cntTroops);
    }

    public TradeType canTradeRiskCards()
    {
        int cntArtillery = 0;
        int cntInfantry = 0;
        int cntCavalry = 0;
        int cntJoker = 0;

        for(RiskCard riskCard : cards)
        {
            switch(riskCard.getType())
            {
                case ARTILLERY -> cntArtillery++;
                case INFANTRY -> cntInfantry++;
                case CAVALRY -> cntCavalry++;
                case JOKER -> cntJoker++;
            }
        }

        return areRiskCardsTradeable(cntArtillery, cntInfantry, cntCavalry, cntJoker);
    }

    private TradeType areRiskCardsTradeable(int cntArtillery, int cntInfantry, int cntCavalry, int cntJoker)
    {
        TradeType tradeType = null;
        if(cntArtillery >= 3)
            tradeType = TradeType.ARTILLERY;
        else if(cntInfantry >= 3)
            tradeType = TradeType.INFANTRY;
        else if(cntCavalry >= 3)
            tradeType = TradeType.CAVALRY;
        else if(cntArtillery + cntJoker >= 3)
            tradeType = TradeType.ARTILLERY_JOKER;
        else if(cntInfantry + cntJoker >= 3)
            tradeType = TradeType.INFANTRY_JOKER;
        else if(cntCavalry + cntJoker >= 3)
            tradeType = TradeType.CAVALRY_JOKER;
        else if(cntArtillery > 0 && cntInfantry > 0 && cntCavalry > 0)
            tradeType = TradeType.MIXED;
        else if(canJokerSubstituteMissingCards(cntArtillery, cntInfantry, cntCavalry, cntJoker))
            tradeType = TradeType.MIXED_JOKER;
        else
            tradeType = TradeType.NONE;
        return tradeType;
    }

    private boolean canJokerSubstituteMissingCards(int cntArtillery, int cntInfantry, int cntCavalry, int cntJoker)
    {
        int riskCardsNeeded = 0;
        if(cntArtillery == 0)
            riskCardsNeeded++;
        if(cntInfantry == 0)
            riskCardsNeeded++;
        if(cntCavalry == 0)
            riskCardsNeeded++;
        return cntJoker >= riskCardsNeeded;
    }

    public void tradeRiskCards() throws RiskException
    {
        TradeType tradeType = canTradeRiskCards();

        if(tradeType.equals(TradeType.NONE))
            throw new RiskException("custom", "Player cannot trade any risk cards");
        tradeCardsByType(cards, tradeType);
    }

    private void tradeCardsByType(ArrayList<RiskCard> riskCards, TradeType tradeType)
    {
        switch (tradeType)
        {
            case ARTILLERY: {
                removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 3);
                break;
            }
            case INFANTRY: {
                removeCardsFromList(riskCards, RiskCardType.INFANTRY, 3);
                break;
            }
            case CAVALRY: {
                removeCardsFromList(riskCards, RiskCardType.CAVALRY, 3);
                break;
            }
            case ARTILLERY_JOKER: {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
                break;
            }
            case INFANTRY_JOKER: {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.INFANTRY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
                break;
            }
            case CAVALRY_JOKER: {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.CAVALRY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
                break;
            }
            case MIXED: {
                removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 1);
                removeCardsFromList(riskCards, RiskCardType.CAVALRY, 1);
                removeCardsFromList(riskCards, RiskCardType.INFANTRY, 1);
                break;
            }
            case MIXED_JOKER: {
                int remainingCards = 0;
                remainingCards += removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 1);
                remainingCards += removeCardsFromList(riskCards, RiskCardType.CAVALRY, 1);
                remainingCards += removeCardsFromList(riskCards, RiskCardType.INFANTRY, 1);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
                break;
            }
            default:
                break;
        }
        cntRiskCardsTraded++;
        getTroopsForTrade();
    }

    private int removeCardsFromList(ArrayList<RiskCard> riskCards, RiskCardType type, int cntCardsToRemove)
    {
        Iterator<RiskCard> riskCardIterator = riskCards.iterator();
        while (riskCardIterator.hasNext()) {
            RiskCard riskCard = riskCardIterator.next();
            if(cntCardsToRemove > 0 && riskCard.getType().equals(type)) {
                riskCardIterator.remove();
                cntCardsToRemove--;
            }
        }
        return  cntCardsToRemove;
    }

    private void getTroopsForTrade()
    {
        if(cntRiskCardsTraded == 1) {
            totalNumberOfTroops += TROOPSFORFIRSTTRADE;
            freeNumberOfTroops += TROOPSFORFIRSTTRADE;
        }
        if(cntRiskCardsTraded == 2) {
            totalNumberOfTroops += TROOPSFORSECONDTRADE;
            freeNumberOfTroops += TROOPSFORSECONDTRADE;
        }
        if(cntRiskCardsTraded == 3) {
            totalNumberOfTroops += TROOPSFORTHIRDTRADE;
            freeNumberOfTroops += TROOPSFORTHIRDTRADE;
        }
        if(cntRiskCardsTraded == 4) {
            totalNumberOfTroops += TROOPSFORFOURTHTRADE;
            freeNumberOfTroops += TROOPSFORFOURTHTRADE;
        }
        if(cntRiskCardsTraded == 5) {
            totalNumberOfTroops += TROOPSFORFIFTHTRADE;
            freeNumberOfTroops += TROOPSFORFIFTHTRADE;
        }
        if(cntRiskCardsTraded == 6) {
            totalNumberOfTroops += TROOPSFORSIXTHTRADE;
            freeNumberOfTroops += TROOPSFORSIXTHTRADE;
        }
        if(cntRiskCardsTraded > 6) {
            totalNumberOfTroops += TROOPSFORSIXTHTRADE + (cntRiskCardsTraded - 6) * 5;
            freeNumberOfTroops += TROOPSFORSIXTHTRADE + (cntRiskCardsTraded - 6) * 5;
        }
    }
}