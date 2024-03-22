package api.src.main.java.se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

public class Player {
    private String name;
    private String color;

    private ArrayList<Troop> army;
    private ArrayList<RiskCard> cards;
    private boolean eliminated;

    public Player(String name, String color, ArrayList<Troop> army, ArrayList<RiskCard> cards) {
        this.name = name;
        this.color = color;
        this.army = army;
        this.cards = cards;
    }

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        cards = new ArrayList<RiskCard>();
        army = new ArrayList<Troop>();
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Troop> getArmy() {
        return army;
    }

    public void setArmy(ArrayList<Troop> army) {
        this.army = army;
    }

    public ArrayList<RiskCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<RiskCard> cards) {
        this.cards = cards;
    }

    public void addArmy(Troop troop)
    {
        army.add(troop);
    }

    public void removeArmy(Troop troop)
    {
        army.remove(troop);
    }

    public void addRiskCard(RiskCard card)
    {
        cards.add(card);
    }

    public void removeRiskCard(RiskCard card)
    {
        cards.remove(card);
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }
}
