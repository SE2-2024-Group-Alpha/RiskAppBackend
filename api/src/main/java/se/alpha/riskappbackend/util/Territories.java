package se.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static final Map<String, TerritoryNode> colorsToTerritories = new HashMap<>();

    private static final HashMap<String, TerritoryNode> territories = new HashMap<>();

    static {
        Alaska.addAdjTerritory(
            new ArrayList<TerritoryNode>() {{
                add(NorthwestTerritory);
                add(Alberta);
                add(Kamchatka);
            }}
        );

        NorthwestTerritory.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Alaska);
                    add(Alberta);
                    add(Ontario);
                    add(Greenland);
                }}
        );

        Greenland.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(NorthwestTerritory);
                    add(Quebec);
                    add(Ontario);
                    add(Iceland);
                }}
        );

        Alberta.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Alaska);
                    add(NorthwestTerritory);
                    add(Ontario);
                    add(WesternUnitedStates);
                }}
        );

        Ontario.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Alberta);
                    add(NorthwestTerritory);
                    add(Quebec);
                    add(Greenland);
                }}
        );

        Quebec.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Ontario);
                    add(EasternUnitedStates);
                    add(Greenland);
                }}
        );

        WesternUnitedStates.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Alberta);
                    add(Ontario);
                    add(EasternUnitedStates);
                    add(CentralAmerica);
                }}
        );

        EasternUnitedStates.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Ontario);
                    add(Quebec);
                    add(WesternUnitedStates);
                    add(CentralAmerica);
                }}
        );


        CentralAmerica.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(WesternUnitedStates);
                    add(EasternUnitedStates);
                    add(Venezuela);
                }}
        );

        Venezuela.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(CentralAmerica);
                    add(Peru);
                    add(Brazil);
                }}
        );

        Peru.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Venezuela);
                    add(Brazil);
                    add(Argentina);
                }}
        );

        Brazil.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Venezuela);
                    add(Peru);
                    add(Argentina);
                    add(NorthAfrica);
                }}
        );

        Argentina.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Brazil);
                    add(Peru);
                }}
        );

        Iceland.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Greenland);
                    add(GreatBritain);
                    add(Scandinavia);
                }}
        );

        Scandinavia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Iceland);
                    add(GreatBritain);
                    add(NorthernEurope);
                    add(Russia);
                }}
        );

        Russia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Scandinavia);
                    add(NorthernEurope);
                    add(Austria);
                    add(MiddleEast);
                    add(Afghanistan);
                    add(Ural);
                }}
        );

        GreatBritain.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Iceland);
                    add(Scandinavia);
                    add(NorthernEurope);
                    add(WesternEurope);
                }}
        );

        NorthernEurope.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Scandinavia);
                    add(GreatBritain);
                    add(WesternEurope);
                    add(Austria);
                    add(Russia);
                }}
        );

        WesternEurope.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(GreatBritain);
                    add(NorthernEurope);
                    add(Austria);
                    add(NorthAfrica);
                }}
        );

        Austria.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(WesternEurope);
                    add(NorthernEurope);
                    add(Russia);
                    add(MiddleEast);
                    add(NorthAfrica);
                    add(Egypt);
                }}
        );

        NorthAfrica.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(WesternEurope);
                    add(Austria);
                    add(Brazil);
                    add(Egypt);
                    add(EastAfrica);
                    add(Congo);
                }}
        );

        Egypt.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(NorthAfrica);
                    add(Austria);
                    add(MiddleEast);
                    add(EastAfrica);
                }}
        );

        EastAfrica.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(NorthAfrica);
                    add(Egypt);
                    add(Congo);
                    add(MiddleEast);
                    add(SouthAfrica);
                    add(Madagascar);
                }}
        );

        Congo.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(NorthAfrica);
                    add(EastAfrica);
                    add(SouthAfrica);
                }}
        );

        SouthAfrica.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Congo);
                    add(EastAfrica);
                    add(Madagascar);
                }}
        );

        Madagascar.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(SouthAfrica);
                    add(EastAfrica);
                }}
        );

        Ural.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Russia);
                    add(Afghanistan);
                    add(China);
                    add(Siberia);
                }}
        );

        Siberia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Ural);
                    add(Yakutsk);
                    add(Irkutsk);
                    add(Mongolia);
                    add(China);
                }}
        );

        Yakutsk.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Siberia);
                    add(Kamchatka);
                    add(Irkutsk);
                }}
        );

        Kamchatka.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Alaska);
                    add(Yakutsk);
                    add(Irkutsk);
                    add(Mongolia);
                    add(Japan);
                }}
        );

        Mongolia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Japan);
                    add(Kamchatka);
                    add(Irkutsk);
                    add(Siberia);
                    add(China);
                }}
        );

        Japan.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Mongolia);
                    add(Kamchatka);
                }}
        );

        Afghanistan.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Ural);
                    add(China);
                    add(India);
                    add(MiddleEast);
                    add(Russia);
                }}
        );

        China.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Mongolia);
                    add(Siberia);
                    add(Ural);
                    add(Afghanistan);
                    add(India);
                    add(Siam);
                }}
        );

        India.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(MiddleEast);
                    add(Afghanistan);
                    add(China);
                    add(Siam);
                }}
        );

        Siam.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(India);
                    add(China);
                    add(Indonesia);
                }}
        );

        Indonesia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Siam);
                    add(NewGuinea);
                    add(WesternAustralia);
                }}
        );

        NewGuinea.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Indonesia);
                    add(WesternAustralia);
                    add(EasternAustralia);
                }}
        );

        WesternAustralia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Indonesia);
                    add(NewGuinea);
                    add(EasternAustralia);
                }}
        );

        EasternAustralia.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Indonesia);
                    add(NewGuinea);
                    add(WesternAustralia);
                }}
        );

        MiddleEast.addAdjTerritory(
                new ArrayList<TerritoryNode>() {{
                    add(Austria);
                    add(Russia);
                    add(Afghanistan);
                    add(India);
                    add(Egypt);
                    add(EastAfrica);
                }}
        );

        colorsToTerritories.put("FF6347", Alaska);
        colorsToTerritories.put("4682B4", NorthwestTerritory);
        colorsToTerritories.put("D8BFD8", Greenland);
        colorsToTerritories.put("FFD700", Alberta);
        colorsToTerritories.put("D2691E", Ontario);
        colorsToTerritories.put("8A2BE2", Quebec);
        colorsToTerritories.put("DC143C", WesternUnitedStates);
        colorsToTerritories.put("00FFFF", EasternUnitedStates);
        colorsToTerritories.put("00008B", CentralAmerica);
        colorsToTerritories.put("008000", Venezuela);
        colorsToTerritories.put("FF4500", Peru);
        colorsToTerritories.put("6A5ACD", Brazil);
        colorsToTerritories.put("8B4513", Argentina);
        colorsToTerritories.put("FA8072", Iceland);
        colorsToTerritories.put("EEE8AA", Scandinavia);
        colorsToTerritories.put("A0522D", Russia);
        colorsToTerritories.put("2E8B57", GreatBritain);
        colorsToTerritories.put("FF1493", NorthernEurope);
        colorsToTerritories.put("483D8B", WesternEurope);
        colorsToTerritories.put("B8860B", Austria);
        colorsToTerritories.put("5F9EA0", NorthAfrica);
        colorsToTerritories.put("9ACD32", Egypt);
        colorsToTerritories.put("8B0000", EastAfrica);
        colorsToTerritories.put("FFA500", Congo);
        colorsToTerritories.put("006400", SouthAfrica);
        colorsToTerritories.put("800080", Madagascar);
        colorsToTerritories.put("FF00FF", Ural);
        colorsToTerritories.put("A52A2A", Siberia);
        colorsToTerritories.put("DEB887", Yakutsk);
        colorsToTerritories.put("85F8FF", Kamchatka);
        colorsToTerritories.put("7FFF00", Irkutsk);
        colorsToTerritories.put("FFDEAD", Mongolia);
        colorsToTerritories.put("FF0000", Japan);
        colorsToTerritories.put("0000CD", Afghanistan);
        colorsToTerritories.put("BA55D3", China);
        colorsToTerritories.put("4B0082", India);
        colorsToTerritories.put("F08080", Siam);
        colorsToTerritories.put("20B2AA", Indonesia);
        colorsToTerritories.put("FFFAF0", NewGuinea);
        colorsToTerritories.put("228B22", WesternAustralia);
        colorsToTerritories.put("ADFF2F", EasternAustralia);
        colorsToTerritories.put("F0E68C", MiddleEast);

        territories.put(Alaska.getName(), Alaska);
        territories.put(NorthwestTerritory.getName(), NorthwestTerritory);
        territories.put(Greenland.getName(), Greenland);
        territories.put(Alberta.getName(), Alberta);
        territories.put(Ontario.getName(), Ontario);
        territories.put(Quebec.getName(), Quebec);
        territories.put(WesternUnitedStates.getName(), WesternUnitedStates);
        territories.put(EasternUnitedStates.getName(), EasternUnitedStates);
        territories.put(CentralAmerica.getName(), CentralAmerica);
        territories.put(Venezuela.getName(), Venezuela);
        territories.put(Peru.getName(), Peru);
        territories.put(Brazil.getName(), Brazil);
        territories.put(Argentina.getName(), Argentina);
        territories.put(Iceland.getName(), Iceland);
        territories.put(Scandinavia.getName(), Scandinavia);
        territories.put(Russia.getName(), Russia);
        territories.put(GreatBritain.getName(), GreatBritain);
        territories.put(NorthernEurope.getName(), NorthernEurope);
        territories.put(WesternEurope.getName(), WesternEurope);
        territories.put(Austria.getName(), Austria);
        territories.put(NorthAfrica.getName(), NorthAfrica);
        territories.put(Egypt.getName(), Egypt);
        territories.put(EastAfrica.getName(), EastAfrica);
        territories.put(Congo.getName(), Congo);
        territories.put(SouthAfrica.getName(), SouthAfrica);
        territories.put(Madagascar.getName(), Madagascar);
        territories.put(Ural.getName(), Ural);
        territories.put(Siberia.getName(), Siberia);
        territories.put(Yakutsk.getName(), Yakutsk);
        territories.put(Kamchatka.getName(), Kamchatka);
        territories.put(Irkutsk.getName(), Irkutsk);
        territories.put(Mongolia.getName(), Mongolia);
        territories.put(Japan.getName(), Japan);
        territories.put(Afghanistan.getName(), Afghanistan);
        territories.put(China.getName(), China);
        territories.put(India.getName(), India);
        territories.put(Siam.getName(), Siam);
        territories.put(Indonesia.getName(), Indonesia);
        territories.put(NewGuinea.getName(), NewGuinea);
        territories.put(WesternAustralia.getName(), WesternAustralia);
        territories.put(EasternAustralia.getName(), EasternAustralia);
        territories.put(MiddleEast.getName(), MiddleEast);
    }


    public static TerritoryNode getTerritoryByColor(String colorKey) {
        return colorsToTerritories.get(colorKey);
    }

    public static HashMap<String, TerritoryNode> getAllTerritories()
    {
        return territories;
    }
}
