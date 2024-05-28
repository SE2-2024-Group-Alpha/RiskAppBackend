package se.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Continent extends Area{
    private List<Country> countries;

    public Continent(String name, Player owner) {
        super(name, owner);
        this.countries = new ArrayList<Country>();
    }

    public void addCountry(Country c)
    {
        countries.add(c);
    }
}
