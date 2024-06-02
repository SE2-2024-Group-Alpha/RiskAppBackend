package se.alpha.riskappbackend.model.db;

import java.io.Serializable;

public class RiskCard implements Serializable {
    private RiskCardType type;
    private Country country;

    public RiskCard(RiskCardType type, Country country) {
        this.type = type;
        this.country = country;
    }

    public RiskCard() {
    }

    public RiskCardType getType() {
        return type;
    }

    public void setType(RiskCardType type) {
        this.type = type;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
