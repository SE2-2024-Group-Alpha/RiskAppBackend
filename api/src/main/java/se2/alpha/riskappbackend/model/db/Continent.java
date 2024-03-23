package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

public class Continent extends Area{
    private ArrayList<Country> countries;

    public Continent(String name, Player owner, ArrayList<Country> countries) {
        super(name, owner);
        this.countries = countries;
    }

    public Continent() {
        super();
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }
}
