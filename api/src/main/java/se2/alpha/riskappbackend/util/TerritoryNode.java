package se2.alpha.riskappbackend.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TerritoryNode {
    private final String name;
    private final String continent;
    private final List<TerritoryNode> adjTerritories;

    public TerritoryNode(String name, String continent) {
        this.name = name;
        this.continent = continent;
        this.adjTerritories = new ArrayList<>();
    }

    public void addAdjTerritory(TerritoryNode territory) {
        this.adjTerritories.add(territory);
    }

    public void addAdjTerritory(List<TerritoryNode> territory) {
        this.adjTerritories.addAll(territory);
    }

    @Override
    public String toString() {
        return this.name;
    }
}