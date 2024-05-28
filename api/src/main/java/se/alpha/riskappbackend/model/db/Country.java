package se.alpha.riskappbackend.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Country extends Area{
    @JsonIgnore
    private ArrayList<Country> attackableCountries;
    @JsonIgnore
    private Continent continent;
    private int numberOfTroops;

    public Country(String name, Player owner, Continent continent) {
        super(name, owner);
        this.continent = continent;
        numberOfTroops = 0;
        attackableCountries = new ArrayList<Country>();
    }

    public Country() {
        super();
    }

    public void addArmy(int newTroops)
    {
        numberOfTroops += newTroops;
    }

    public void removeArmy(int removedTroops)
    {
        numberOfTroops -= removedTroops;
    }

    public void addAttackableCountry(Country country)
    {
        attackableCountries.add(country);
    }
}
