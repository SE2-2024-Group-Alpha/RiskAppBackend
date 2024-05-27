package se.alpha.riskappbackend.model.db;

public abstract class Area {
    private String name;
    private Player owner;

    public Area(String name, Player owner) {
        this.name = name;
        this.owner = owner;
    }

    protected Area() {
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
