package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Country extends Area{
    private ArrayList<Country> attackableCountries;
    private Continent continent;
    private ArrayList<Troop> army;

    public Country(String name, Player owner, Continent continent) {
        super(name, owner);
        this.continent = continent;
        this.continent.addCountry(this);
        army = new ArrayList<Troop>();
        attackableCountries = new ArrayList<Country>();
    }

    public Country() {
        super();
    }

    public void setAttackableCountries(ArrayList<Country> attackableCountries) {
        this.attackableCountries = attackableCountries;
    }

    public void addArmy(Troop t)
    {
        army.add(t);
        t.setLocation(this);
    }

    public void removeArmy(Troop t)
    {
        army.remove(t);
        t.setLocation(null);
    }

    public void addAttackableCountry(Country country)
    {
        attackableCountries.add(country);
    }

    @Override
    public Object clone() {
        try {
            Country cloned = (Country) super.clone();
            cloned.attackableCountries = new ArrayList<>();
            for (Country country : this.attackableCountries) {
                cloned.attackableCountries.add((Country) country.clone());
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
