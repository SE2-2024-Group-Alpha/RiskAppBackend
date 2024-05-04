package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.Iterator;

import lombok.Getter;

@Getter
public class Player {
    private String id;
    private String name;
    private String color;
    private ArrayList<Troop> army;
    private ArrayList<RiskCard> cards;
    private boolean eliminated;
    private int cntRiskCardsTraded;
    private ArrayList<Country> controlledCountries;
    private ArrayList<Continent> controlledContinents;
    private static final int TROOPSFORFIRSTTRADE = 4;
    private static final int TROOPSFORSECONDTRADE = 6;
    private static final int TROOPSFORTHIRDTRADE = 8;
    private static final int TROOPSFORFOURTHTRADE = 10;
    private static final int TROOPSFORFIFTHTRADE = 12;
    private static final int TROOPSFORSIXTHTRADE = 15;

    public Player(String id, String name, String color, ArrayList<Troop> army, ArrayList<RiskCard> cards) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.army = army;
        this.cards = cards;
        controlledContinents = new ArrayList<Continent>();
        controlledCountries = new ArrayList<Country>();
    }

    public Player(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        cards = new ArrayList<RiskCard>();
        army = new ArrayList<Troop>();
        controlledContinents = new ArrayList<Continent>();
        controlledCountries = new ArrayList<Country>();
    }

    public Player() {
        cards = new ArrayList<RiskCard>();
        army = new ArrayList<Troop>();
        controlledContinents = new ArrayList<Continent>();
        controlledCountries = new ArrayList<Country>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setArmy(ArrayList<Troop> army) {
        this.army = army;
    }

    public void setCards(ArrayList<RiskCard> cards) {
        this.cards = cards;
    }

    public void setCntRiskCardsTraded(int cntRiskCardsTraded)
    {
        this.cntRiskCardsTraded = cntRiskCardsTraded;
    }

    public void addArmy(Troop troop)
    {
        army.add(troop);
        troop.setOwner(this);
    }

    public void removeArmy(Troop troop)
    {
        army.remove(troop);
        troop.setOwner(null);
    }

    public void addRiskCard(RiskCard card)
    {
        cards.add(card);
    }

    public void removeRiskCard(RiskCard card)
    {
        cards.remove(card);
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
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

    public TradeType canTradeRiskCards() throws Exception
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

    public void tradeRiskCards() throws Exception
    {
        TradeType tradeType = canTradeRiskCards();

        if(tradeType.equals(TradeType.NONE))
            throw new Exception("Player cannot trade any risk cards");
        tradeCardsByType(cards, tradeType);
    }

    private void tradeCardsByType(ArrayList<RiskCard> riskCards, TradeType tradeType)
    {
        switch (tradeType)
        {
            case ARTILLERY -> {
                removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 3);
            }
            case INFANTRY -> {
                removeCardsFromList(riskCards, RiskCardType.INFANTRY, 3);
            }
            case CAVALRY -> {
                removeCardsFromList(riskCards, RiskCardType.CAVALRY, 3);
            }
            case ARTILLERY_JOKER -> {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
            }
            case INFANTRY_JOKER -> {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.INFANTRY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
            }
            case CAVALRY_JOKER -> {
                int remainingCards = removeCardsFromList(riskCards, RiskCardType.CAVALRY, 3);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
            }
            case MIXED -> {
                removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 1);
                removeCardsFromList(riskCards, RiskCardType.CAVALRY, 1);
                removeCardsFromList(riskCards, RiskCardType.INFANTRY, 1);
            }
            case MIXED_JOKER -> {
                int remainingCards = 0;
                remainingCards += removeCardsFromList(riskCards, RiskCardType.ARTILLERY, 1);
                remainingCards += removeCardsFromList(riskCards, RiskCardType.CAVALRY, 1);
                remainingCards += removeCardsFromList(riskCards, RiskCardType.INFANTRY, 1);
                removeCardsFromList(riskCards, RiskCardType.JOKER, remainingCards);
            }
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
        int cntNewTroops = getNumberOfNewTroopsForTrade();
        for(int i = 0; i < cntNewTroops; i++)
        {
            army.add(new Troop(TroopType.INFANTRY, null, this));
        }
    }

    private int getNumberOfNewTroopsForTrade()
    {
        int cntNewTroops = 0;
        if(cntRiskCardsTraded == 1)
            cntNewTroops = TROOPSFORFIRSTTRADE;
        if(cntRiskCardsTraded == 2)
            cntNewTroops = TROOPSFORSECONDTRADE;
        if(cntRiskCardsTraded == 3)
            cntNewTroops = TROOPSFORTHIRDTRADE;
        if(cntRiskCardsTraded == 4)
            cntNewTroops = TROOPSFORFOURTHTRADE;
        if(cntRiskCardsTraded == 5)
            cntNewTroops = TROOPSFORFIFTHTRADE;
        if(cntRiskCardsTraded == 6)
            cntNewTroops = TROOPSFORSIXTHTRADE;
        if(cntRiskCardsTraded > 6)
            cntNewTroops = TROOPSFORSIXTHTRADE + (cntRiskCardsTraded - 6) * 5;

        return cntNewTroops;
    }
}