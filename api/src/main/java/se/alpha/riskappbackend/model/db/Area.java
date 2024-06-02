package se.alpha.riskappbackend.model.db;

import java.io.Serializable;

public abstract class Area implements Serializable {
    private String name;
    private Player owner;

    protected Area(String name, Player owner) {
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
