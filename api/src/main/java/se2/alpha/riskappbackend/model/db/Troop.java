package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

public class Troop {
    private TroopType type;
    private Country location;

    public Troop(TroopType type, Country location) {
        this.type = type;
        this.location = location;
    }

    public Troop() {
    }

    public TroopType getType() {
        return type;
    }

    public void setType(TroopType type) {
        this.type = type;
    }

    public Country getLocation() {
        return location;
    }

    public void setLocation(Country location) {
        this.location = location;
    }
}
