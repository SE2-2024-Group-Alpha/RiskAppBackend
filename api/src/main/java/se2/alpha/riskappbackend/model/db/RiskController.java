package se2.alpha.riskappbackend.model.db;

import java.util.ArrayList;

public class RiskController {
    private ArrayList<Player> players;
    private Board board;

    public RiskController(ArrayList<Player> players, Board board) {
        this.players = players;
        this.board = board;
    }

    public RiskController() {
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public RiskCard getNewRiskCard() throws Exception
    {
        return board.getNewRiskCard();
    }

    public ArrayList<RiskCard> getRiskCardsByPlayer(String id) throws Exception
    {
        for(Player player : players)
        {
            if(player.getId().equals(id))
                return player.getCards();
        }
        throw new Exception("no player with this id found");
    }

    public boolean canPlayerTradeRiskCards(String id) throws Exception
    {
        Player player = getPlayerById(id);
        return !player.canTradeRiskCards().equals(TradeType.NONE);
    }

    public void tradeRiskCards(String id) throws Exception
    {
        Player player = getPlayerById(id);
        TradeType tradeType = player.canTradeRiskCards();
        if(tradeType.equals(TradeType.NONE))
            throw new Exception("Player cannot trade any risk cards");
        player.tradeRiskCards();
    }

    private Player getPlayerById(String id) throws Exception
    {
        for(Player player : players)
        {
            if(player.getId().equals(id))
                return player;
        }
        throw new Exception("no player with this id found");
    }
}
