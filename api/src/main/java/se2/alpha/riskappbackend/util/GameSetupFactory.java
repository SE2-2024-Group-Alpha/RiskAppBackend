package se2.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import se2.alpha.riskappbackend.model.db.Continent;
import se2.alpha.riskappbackend.model.db.Country;
import se2.alpha.riskappbackend.model.db.RiskCard;
import se2.alpha.riskappbackend.model.db.RiskCardType;

public class GameSetupFactory {
    private static ArrayList<Continent> continents;
    private static ArrayList<RiskCard> riskCards;
    public static ArrayList<Continent> getContinents() throws Exception {
        if(continents == null)
            setupContinents();
        return (ArrayList<Continent>) continents.clone();
    }

    public static ArrayList<RiskCard> getRiskCards() throws Exception {
        if(riskCards == null)
            setupRiskCards();
        return (ArrayList<RiskCard>) riskCards.clone();
    }
    private static void setupContinents() throws Exception
    {
        HashMap<String, TerritoryNode> territories = Territories.getAllTerritories();
        for (Map.Entry<String, TerritoryNode> entry : territories.entrySet()) {
            String territoryName = entry.getKey();
            TerritoryNode territoryNode = entry.getValue();
            if(isNewContinent(territoryName))
                continents.add(new Continent(territoryName, null));
            setupCountry(territoryNode, territoryName);
        }

        setAttackableCountries(territories);
    }

    private static void setAttackableCountries(HashMap<String, TerritoryNode> territories) throws Exception {
        for(Country country : getAllCountries())
        {
            for(TerritoryNode territoryNode : territories.get(country.getName()).getAdjTerritories())
            {
                country.addAttackableCountry(getCountryByName(territoryNode.getName()));
            }

        }
    }

    private static void setupCountry(TerritoryNode territoryNode, String territoryName) throws Exception {
        Continent continent = getContinentByName(territoryNode.getContinent());
        continent.addCountry(new Country(territoryName, null, continent));
    }

    private static boolean isNewContinent(String name)
    {
        for(Continent continent : continents)
        {
            if(continent.getName().equals(name))
                return true;
        }
        return false;
    }

    private static Continent getContinentByName(String name) throws Exception
    {
        for(Continent continent : continents)
        {
            if(continent.getName().equals(name))
                return continent;
        }
        throw new Exception("Continent not found");
    }

    private static void setupRiskCards() throws Exception {
        if(continents == null)
            setupContinents();
        ArrayList<Country> countries = getAllCountries();

        for(int i = 0; i < countries.size(); i++)
        {
            if(i < countries.size() / 3)
                riskCards.add(setupArtilleryRiskCard(countries.get(i)));
            else if(i < countries.size() / 3 * 2)
                riskCards.add(setupCavalryRiskCard(countries.get(i)));
            else
                riskCards.add(setuInfantryRiskCard(countries.get(i)));
        }
    }
    private static RiskCard setupArtilleryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.ARTILLERY, country);
    }
    private static RiskCard setuInfantryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.INFANTRY, country);
    }
    private static RiskCard setupCavalryRiskCard(Country country)
    {
        return new RiskCard(RiskCardType.CAVALRY, country);
    }
    private static ArrayList<Country> getAllCountries()
    {
        ArrayList<Country> countries = new ArrayList<Country>();
        for(Continent continent : continents)
        {
            countries.addAll(continent.getCountries());
        }
        return countries;
    }

    private static Country getCountryByName(String name) throws Exception
    {
        for(Country country : getAllCountries())
        {
            if(country.getName().equals(name))
                return country;
        }
        throw new Exception("Country not found");
    }
}
