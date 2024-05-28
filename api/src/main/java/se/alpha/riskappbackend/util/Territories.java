package se.alpha.riskappbackend.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Territories {
    private static final String northAmerica = "North America";
    private static final String southAmerica = "South America";
    private static final String europe = "Europe";
    private static final String africa = "Africa";
    private static final String asia = "Asia";
    private static final String oceania = "Oceania";
    private static final TerritoryNode Alaska = new TerritoryNode("Alaska", northAmerica);
    private static final TerritoryNode NorthwestTerritory = new TerritoryNode("Northwest Territory", northAmerica);
    private static final TerritoryNode Greenland = new TerritoryNode("Greenland", northAmerica);
    private static final TerritoryNode Alberta = new TerritoryNode("Alberta", northAmerica);
    private static final TerritoryNode Ontario = new TerritoryNode("Ontario", northAmerica);
    private static final TerritoryNode Quebec = new TerritoryNode("Quebec", northAmerica);
    private static final TerritoryNode WesternUnitedStates = new TerritoryNode("Western United States", northAmerica);
    private static final TerritoryNode EasternUnitedStates = new TerritoryNode("Eastern United States", northAmerica);
    private static final TerritoryNode CentralAmerica = new TerritoryNode("Central America", northAmerica);
    private static final TerritoryNode Venezuela = new TerritoryNode("Venezuela", southAmerica);
    private static final TerritoryNode Peru = new TerritoryNode("Peru", southAmerica);
    private static final TerritoryNode Brazil = new TerritoryNode("Brazil", southAmerica);
    private static final TerritoryNode Argentina = new TerritoryNode("Argentina", southAmerica);
    private static final TerritoryNode Iceland = new TerritoryNode("Iceland", europe);
    private static final TerritoryNode Scandinavia = new TerritoryNode("Scandinavia", europe);
    private static final TerritoryNode Russia = new TerritoryNode("Russia", europe);
    private static final TerritoryNode GreatBritain = new TerritoryNode("Great Britain", europe);
    private static final TerritoryNode NorthernEurope = new TerritoryNode("Northern Europe", europe);
    private static final TerritoryNode WesternEurope = new TerritoryNode("Western Europe", europe);
    private static final TerritoryNode Austria = new TerritoryNode("Austria", europe);
    private static final TerritoryNode NorthAfrica = new TerritoryNode("North Africa", africa);
    private static final TerritoryNode Egypt = new TerritoryNode("Egypt", africa);
    private static final TerritoryNode EastAfrica = new TerritoryNode("East Africa", africa);
    private static final TerritoryNode Congo = new TerritoryNode("Congo", africa);
    private static final TerritoryNode SouthAfrica = new TerritoryNode("South Africa", africa);
    private static final TerritoryNode Madagascar = new TerritoryNode("Madagascar", africa);
    private static final TerritoryNode Ural = new TerritoryNode("Ural", asia);
    private static final TerritoryNode Siberia = new TerritoryNode("Siberia", asia);
    private static final TerritoryNode Yakutsk = new TerritoryNode("Yakutsk", asia);
    private static final TerritoryNode Kamchatka = new TerritoryNode("Kamchatka", asia);
    private static final TerritoryNode Irkutsk = new TerritoryNode("Irkutsk", asia);
    private static final TerritoryNode Mongolia = new TerritoryNode("Mongolia", asia);
    private static final TerritoryNode Japan = new TerritoryNode("Japan", asia);
    private static final TerritoryNode Afghanistan = new TerritoryNode("Afghanistan", asia);
    private static final TerritoryNode China = new TerritoryNode("China", asia);
    private static final TerritoryNode India = new TerritoryNode("India", asia);
    private static final TerritoryNode Siam = new TerritoryNode("Siam", asia);
    private static final TerritoryNode Indonesia = new TerritoryNode("Indonesia", oceania);
    private static final TerritoryNode NewGuinea = new TerritoryNode("New Guinea", oceania);
    private static final TerritoryNode WesternAustralia = new TerritoryNode("Western Australia", oceania);
    private static final TerritoryNode EasternAustralia = new TerritoryNode("Eastern Australia", oceania);
    private static final TerritoryNode MiddleEast = new TerritoryNode("Middle East", asia);

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
