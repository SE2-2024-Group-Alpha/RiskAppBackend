package se2.alpha.riskappbackend.model.db;

public class RiskCard {
    private TroopType type;
    private Country country;
    private boolean isWildCard;

    public RiskCard(TroopType type, Country country, boolean isWildCard) {
        this.type = type;
        this.country = country;
        this.isWildCard = isWildCard;
    }

    public RiskCard() {
    }

    public TroopType getType() {
        return type;
    }

    public void setType(TroopType type) {
        this.type = type;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isWildCard() {
        return isWildCard;
    }

    public void setWildCard(boolean wildCard) {
        isWildCard = wildCard;
    }
}
