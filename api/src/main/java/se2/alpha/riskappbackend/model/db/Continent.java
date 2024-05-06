package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public Object clone() {
        try {
            Continent cloned = (Continent) super.clone();
            cloned.countries = new ArrayList<>();
            for (Country country : this.countries) {
                cloned.countries.add((Country) country.clone());
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
