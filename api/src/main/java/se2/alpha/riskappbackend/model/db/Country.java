package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

public class Country extends Area{
    private ArrayList<Country> attackableCountries;

    public Country(String name, Player owner, ArrayList<Country> attackableCountries) {
        super(name, owner);
        this.attackableCountries = attackableCountries;
    }

    public Country() {
        super();
    }

    public ArrayList<Country> getAttackableCountries() {
        return attackableCountries;
    }

    public void setAttackableCountries(ArrayList<Country> attackableCountries) {
        this.attackableCountries = attackableCountries;
    }
}
