package se2.alpha.riskappbackend.model.db;

import lombok.Getter;

@Getter
public class Troop {
    private TroopType type;
    private Country location;
    private Player owner;

    public Troop(TroopType type, Country location, Player owner) {
        this.type = type;
        this.location = location;
        this.owner = owner;
    }

    public Troop() {
    }

    public void setType(TroopType type) {
        this.type = type;
    }

    public void setLocation(Country location) {
        this.location = location;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
