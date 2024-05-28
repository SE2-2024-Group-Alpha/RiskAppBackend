package se.alpha.riskappbackend.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Territories {
    private static final TerritoryNode Alaska = new TerritoryNode("Alaska", "North America");
    private static final TerritoryNode NorthwestTerritory = new TerritoryNode("Northwest Territory", "North America");
    private static final TerritoryNode Greenland = new TerritoryNode("Greenland", "North America");
    private static final TerritoryNode Alberta = new TerritoryNode("Alberta", "North America");
    private static final TerritoryNode Ontario = new TerritoryNode("Ontario", "North America");
    private static final TerritoryNode Quebec = new TerritoryNode("Quebec", "North America");
    private static final TerritoryNode WesternUnitedStates = new TerritoryNode("Western United States", "North America");
    private static final TerritoryNode EasternUnitedStates = new TerritoryNode("Eastern United States", "North America");
    private static final TerritoryNode CentralAmerica = new TerritoryNode("Central America", "North America");
    private static final TerritoryNode Venezuela = new TerritoryNode("Venezuela", "South America");
    private static final TerritoryNode Peru = new TerritoryNode("Peru", "South America");
    private static final TerritoryNode Brazil = new TerritoryNode("Brazil", "South America");
    private static final TerritoryNode Argentina = new TerritoryNode("Argentina", "South America");
    private static final TerritoryNode Iceland = new TerritoryNode("Iceland", "Europe");
    private static final TerritoryNode Scandinavia = new TerritoryNode("Scandinavia", "Europe");
    private static final TerritoryNode Russia = new TerritoryNode("Russia", "Europe");
    private static final TerritoryNode GreatBritain = new TerritoryNode("Great Britain", "Europe");
    private static final TerritoryNode NorthernEurope = new TerritoryNode("Northern Europe", "Europe");
    private static final TerritoryNode WesternEurope = new TerritoryNode("Western Europe", "Europe");
    private static final TerritoryNode Austria = new TerritoryNode("Austria", "Europe");
    private static final TerritoryNode NorthAfrica = new TerritoryNode("North Africa", "Africa");
    private static final TerritoryNode Egypt = new TerritoryNode("Egypt", "Africa");
    private static final TerritoryNode EastAfrica = new TerritoryNode("East Africa", "Africa");
    private static final TerritoryNode Congo = new TerritoryNode("Congo", "Africa");
    private static final TerritoryNode SouthAfrica = new TerritoryNode("South Africa", "Africa");
    private static final TerritoryNode Madagascar = new TerritoryNode("Madagascar", "Africa");
    private static final TerritoryNode Ural = new TerritoryNode("Ural", "Asia");
    private static final TerritoryNode Siberia = new TerritoryNode("Siberia", "Asia");
    private static final TerritoryNode Yakutsk = new TerritoryNode("Yakutsk", "Asia");
    private static final TerritoryNode Kamchatka = new TerritoryNode("Kamchatka", "Asia");
    private static final TerritoryNode Irkutsk = new TerritoryNode("Irkutsk", "Asia");
    private static final TerritoryNode Mongolia = new TerritoryNode("Mongolia", "Asia");
    private static final TerritoryNode Japan = new TerritoryNode("Japan", "Asia");
    private static final TerritoryNode Afghanistan = new TerritoryNode("Afghanistan", "Asia");
    private static final TerritoryNode China = new TerritoryNode("China", "Asia");
    private static final TerritoryNode India = new TerritoryNode("India", "Asia");
    private static final TerritoryNode Siam = new TerritoryNode("Siam", "Asia");
    private static final TerritoryNode Indonesia = new TerritoryNode("Indonesia", "Oceania");
    private static final TerritoryNode NewGuinea = new TerritoryNode("New Guinea", "Oceania");
    private static final TerritoryNode WesternAustralia = new TerritoryNode("Western Australia", "Oceania");
    private static final TerritoryNode EasternAustralia = new TerritoryNode("Eastern Australia", "Oceania");
    private static final TerritoryNode MiddleEast = new TerritoryNode("Middle East", "Asia");

    private static final HashMap<String, TerritoryNode> territories = new HashMap<>();

    static {
        Alaska.addAdjTerritory(Arrays.asList(NorthwestTerritory, Alberta, Kamchatka));

        NorthwestTerritory.addAdjTerritory(Arrays.asList(Alaska, Alberta, Ontario, Greenland));

        Greenland.addAdjTerritory(Arrays.asList(NorthwestTerritory, Quebec, Ontario, Iceland));

        Alberta.addAdjTerritory(Arrays.asList(Alaska, NorthwestTerritory, Ontario, WesternUnitedStates));

        Ontario.addAdjTerritory(Arrays.asList(Alberta, NorthwestTerritory, Quebec, Greenland));

        Quebec.addAdjTerritory(Arrays.asList(Ontario, EasternUnitedStates, Greenland));

        WesternUnitedStates.addAdjTerritory(Arrays.asList(Alberta, Ontario, EasternUnitedStates, CentralAmerica));

        EasternUnitedStates.addAdjTerritory(Arrays.asList(Ontario, Quebec, WesternUnitedStates, CentralAmerica));

        CentralAmerica.addAdjTerritory(Arrays.asList(WesternUnitedStates, EasternUnitedStates, Venezuela));

        Venezuela.addAdjTerritory(Arrays.asList(CentralAmerica, Peru, Brazil));

        Peru.addAdjTerritory(Arrays.asList(Venezuela, Brazil, Argentina));

        Brazil.addAdjTerritory(Arrays.asList(Venezuela, Peru, Argentina, NorthAfrica));

        Argentina.addAdjTerritory(Arrays.asList(Brazil, Peru));

        Iceland.addAdjTerritory(Arrays.asList(Greenland, GreatBritain, Scandinavia));

        Scandinavia.addAdjTerritory(Arrays.asList(Iceland, GreatBritain, NorthernEurope, Russia));

        Russia.addAdjTerritory(Arrays.asList(Scandinavia, NorthernEurope, Austria, MiddleEast, Afghanistan, Ural));

        GreatBritain.addAdjTerritory(Arrays.asList(Iceland, Scandinavia, NorthernEurope, WesternEurope));

        NorthernEurope.addAdjTerritory(Arrays.asList(Scandinavia, GreatBritain, WesternEurope, Austria, Russia));

        WesternEurope.addAdjTerritory(Arrays.asList(GreatBritain, NorthernEurope, Austria, NorthAfrica));

        Austria.addAdjTerritory(Arrays.asList(WesternEurope, NorthernEurope, Russia, MiddleEast, NorthAfrica, Egypt));

        NorthAfrica.addAdjTerritory(Arrays.asList(WesternEurope, Austria, Brazil, Egypt, EastAfrica, Congo));

        Egypt.addAdjTerritory(Arrays.asList(NorthAfrica, Austria, MiddleEast, EastAfrica));

        EastAfrica.addAdjTerritory(Arrays.asList(NorthAfrica, Egypt, Congo, MiddleEast, SouthAfrica, Madagascar));

        Congo.addAdjTerritory(Arrays.asList(NorthAfrica, EastAfrica, SouthAfrica));

        SouthAfrica.addAdjTerritory(Arrays.asList(Congo, EastAfrica, Madagascar));

        Madagascar.addAdjTerritory(Arrays.asList(SouthAfrica, EastAfrica));

        Ural.addAdjTerritory(Arrays.asList(Russia, Afghanistan, China, Siberia));

        Siberia.addAdjTerritory(Arrays.asList(Ural, Yakutsk, Irkutsk, Mongolia, China));

        Yakutsk.addAdjTerritory(Arrays.asList(Siberia, Kamchatka, Irkutsk));

        Kamchatka.addAdjTerritory(Arrays.asList(Alaska, Yakutsk, Irkutsk, Mongolia, Japan));

        Mongolia.addAdjTerritory(Arrays.asList(Japan, Kamchatka, Irkutsk, Siberia, China));

        Japan.addAdjTerritory(Arrays.asList(Mongolia, Kamchatka));

        Afghanistan.addAdjTerritory(Arrays.asList(Ural, China, India, MiddleEast, Russia));

        China.addAdjTerritory(Arrays.asList(Mongolia, Siberia, Ural, Afghanistan, India, Siam));

        India.addAdjTerritory(Arrays.asList(MiddleEast, Afghanistan, China, Siam));

        Siam.addAdjTerritory(Arrays.asList(India, China, Indonesia));

        Indonesia.addAdjTerritory(Arrays.asList(Siam, NewGuinea, WesternAustralia));

        NewGuinea.addAdjTerritory(Arrays.asList(Indonesia, WesternAustralia, EasternAustralia));

        WesternAustralia.addAdjTerritory(Arrays.asList(Indonesia, NewGuinea, EasternAustralia));

        EasternAustralia.addAdjTerritory(Arrays.asList(Indonesia, NewGuinea, WesternAustralia));

        MiddleEast.addAdjTerritory(Arrays.asList(Austria, Russia, Afghanistan, India, Egypt, EastAfrica));

        territories.put("Alaska", Alaska);
        territories.put("Northwest Territory", NorthwestTerritory);
        territories.put("Greenland", Greenland);
        territories.put("Alberta", Alberta);
        territories.put("Ontario", Ontario);
        territories.put("Quebec", Quebec);
        territories.put("Western United States", WesternUnitedStates);
        territories.put("Eastern United States", EasternUnitedStates);
        territories.put("Central America", CentralAmerica);
        territories.put("Venezuela", Venezuela);
        territories.put("Peru", Peru);
        territories.put("Brazil", Brazil);
        territories.put("Argentina", Argentina);
        territories.put("Iceland", Iceland);
        territories.put("Scandinavia", Scandinavia);
        territories.put("Russia", Russia);
        territories.put("Great Britain", GreatBritain);
        territories.put("Northern Europe", NorthernEurope);
        territories.put("Western Europe", WesternEurope);
        territories.put("Austria", Austria);
        territories.put("North Africa", NorthAfrica);
        territories.put("Egypt", Egypt);
        territories.put("East Africa", EastAfrica);
        territories.put("Congo", Congo);
        territories.put("South Africa", SouthAfrica);
        territories.put("Madagascar", Madagascar);
        territories.put("Ural", Ural);
        territories.put("Siberia", Siberia);
        territories.put("Yakutsk", Yakutsk);
        territories.put("Kamchatka", Kamchatka);
        territories.put("Irkutsk", Irkutsk);
        territories.put("Mongolia", Mongolia);
        territories.put("Japan", Japan);
        territories.put("Afghanistan", Afghanistan);
        territories.put("China", China);
        territories.put("India", India);
        territories.put("Siam", Siam);
        territories.put("Indonesia", Indonesia);
        territories.put("New Guinea", NewGuinea);
        territories.put("Western Australia", WesternAustralia);
        territories.put("Eastern Australia", EasternAustralia);
        territories.put("Middle East", MiddleEast);
    }

    public static HashMap<String, TerritoryNode> getAllTerritories()
    {
        return territories;
    }
}
