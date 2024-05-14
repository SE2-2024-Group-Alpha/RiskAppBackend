package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Continent extends Area{
    private ArrayList<Country> countries;

    public Continent(String name, Player owner) {
        super(name, owner);
        this.countries = new ArrayList<Country>();
    }

    public Continent() {
        super();
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public void addCountry(Country c)
    {
        countries.add(c);
    }
}
