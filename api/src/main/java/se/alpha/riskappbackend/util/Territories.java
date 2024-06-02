package se.alpha.riskappbackend.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Territories {
    private static final String NorthAmerica = "North America";
    private static final String SouthAmerica = "South America";
    private static final String Europe = "Europe";
    private static final String Africa = "Africa";
    private static final String Asia = "Asia";
    private static final String Oceania = "Oceania";
    private static final TerritoryNode Alaska = new TerritoryNode("Alaska", NorthAmerica);
    private static final TerritoryNode NorthwestTerritory = new TerritoryNode("Northwest Territory", NorthAmerica);
    private static final TerritoryNode Greenland = new TerritoryNode("Greenland", NorthAmerica);
    private static final TerritoryNode Alberta = new TerritoryNode("Alberta", NorthAmerica);
    private static final TerritoryNode Ontario = new TerritoryNode("Ontario", NorthAmerica);
    private static final TerritoryNode Quebec = new TerritoryNode("Quebec", NorthAmerica);
    private static final TerritoryNode WesternUnitedStates = new TerritoryNode("Western United States", NorthAmerica);
    private static final TerritoryNode EasternUnitedStates = new TerritoryNode("Eastern United States", NorthAmerica);
    private static final TerritoryNode CentralAmerica = new TerritoryNode("Central America", NorthAmerica);
    private static final TerritoryNode Venezuela = new TerritoryNode("Venezuela", SouthAmerica);
    private static final TerritoryNode Peru = new TerritoryNode("Peru", SouthAmerica);
    private static final TerritoryNode Brazil = new TerritoryNode("Brazil", SouthAmerica);
    private static final TerritoryNode Argentina = new TerritoryNode("Argentina", SouthAmerica);
    private static final TerritoryNode Iceland = new TerritoryNode("Iceland", Europe);
    private static final TerritoryNode Scandinavia = new TerritoryNode("Scandinavia", Europe);
    private static final TerritoryNode Russia = new TerritoryNode("Russia", Europe);
    private static final TerritoryNode GreatBritain = new TerritoryNode("Great Britain", Europe);
    private static final TerritoryNode NorthernEurope = new TerritoryNode("Northern Europe", Europe);
    private static final TerritoryNode WesternEurope = new TerritoryNode("Western Europe", Europe);
    private static final TerritoryNode Austria = new TerritoryNode("Austria", Europe);
    private static final TerritoryNode NorthAfrica = new TerritoryNode("North Africa", Africa);
    private static final TerritoryNode Egypt = new TerritoryNode("Egypt", Africa);
    private static final TerritoryNode EastAfrica = new TerritoryNode("East Africa", Africa);
    private static final TerritoryNode Congo = new TerritoryNode("Congo", Africa);
    private static final TerritoryNode SouthAfrica = new TerritoryNode("South Africa", Africa);
    private static final TerritoryNode Madagascar = new TerritoryNode("Madagascar", Africa);
    private static final TerritoryNode Ural = new TerritoryNode("Ural", Asia);
    private static final TerritoryNode Siberia = new TerritoryNode("Siberia", Asia);
    private static final TerritoryNode Yakutsk = new TerritoryNode("Yakutsk", Asia);
    private static final TerritoryNode Kamchatka = new TerritoryNode("Kamchatka", Asia);
    private static final TerritoryNode Irkutsk = new TerritoryNode("Irkutsk", Asia);
    private static final TerritoryNode Mongolia = new TerritoryNode("Mongolia", Asia);
    private static final TerritoryNode Japan = new TerritoryNode("Japan", Asia);
    private static final TerritoryNode Afghanistan = new TerritoryNode("Afghanistan", Asia);
    private static final TerritoryNode China = new TerritoryNode("China", Asia);
    private static final TerritoryNode India = new TerritoryNode("India", Asia);
    private static final TerritoryNode Siam = new TerritoryNode("Siam", Asia);
    private static final TerritoryNode Indonesia = new TerritoryNode("Indonesia", Oceania);
    private static final TerritoryNode NewGuinea = new TerritoryNode("New Guinea", Oceania);
    private static final TerritoryNode WesternAustralia = new TerritoryNode("Western Australia", Oceania);
    private static final TerritoryNode EasternAustralia = new TerritoryNode("Eastern Australia", Oceania);
    private static final TerritoryNode MiddleEast = new TerritoryNode("Middle East", Asia);

    private Territories(){
    }

    private static final HashMap<String, TerritoryNode> territoryMap = new HashMap<>();

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

        territoryMap.put("Alaska", Alaska);
        territoryMap.put("Northwest Territory", NorthwestTerritory);
        territoryMap.put("Greenland", Greenland);
        territoryMap.put("Alberta", Alberta);
        territoryMap.put("Ontario", Ontario);
        territoryMap.put("Quebec", Quebec);
        territoryMap.put("Western United States", WesternUnitedStates);
        territoryMap.put("Eastern United States", EasternUnitedStates);
        territoryMap.put("Central America", CentralAmerica);
        territoryMap.put("Venezuela", Venezuela);
        territoryMap.put("Peru", Peru);
        territoryMap.put("Brazil", Brazil);
        territoryMap.put("Argentina", Argentina);
        territoryMap.put("Iceland", Iceland);
        territoryMap.put("Scandinavia", Scandinavia);
        territoryMap.put("Russia", Russia);
        territoryMap.put("Great Britain", GreatBritain);
        territoryMap.put("Northern Europe", NorthernEurope);
        territoryMap.put("Western Europe", WesternEurope);
        territoryMap.put("Austria", Austria);
        territoryMap.put("North Africa", NorthAfrica);
        territoryMap.put("Egypt", Egypt);
        territoryMap.put("East Africa", EastAfrica);
        territoryMap.put("Congo", Congo);
        territoryMap.put("South Africa", SouthAfrica);
        territoryMap.put("Madagascar", Madagascar);
        territoryMap.put("Ural", Ural);
        territoryMap.put("Siberia", Siberia);
        territoryMap.put("Yakutsk", Yakutsk);
        territoryMap.put("Kamchatka", Kamchatka);
        territoryMap.put("Irkutsk", Irkutsk);
        territoryMap.put("Mongolia", Mongolia);
        territoryMap.put("Japan", Japan);
        territoryMap.put("Afghanistan", Afghanistan);
        territoryMap.put("China", China);
        territoryMap.put("India", India);
        territoryMap.put("Siam", Siam);
        territoryMap.put("Indonesia", Indonesia);
        territoryMap.put("New Guinea", NewGuinea);
        territoryMap.put("Western Australia", WesternAustralia);
        territoryMap.put("Eastern Australia", EasternAustralia);
        territoryMap.put("Middle East", MiddleEast);
    }

    public static Map<String, TerritoryNode> getAllTerritories()
    {
        return territoryMap;
    }
}
