package poker.socket.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * A class to represent a single game on the server
 */
public class Game {
    private int id;
    private int ante;
    private int pot;
    private int maxBid;
    private int maxPlayersNumber;
    private int startingMoney;
    public Deck deck = new Deck();
    private Player dealer;
    private Player playerTurn;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> activePlayers = new ArrayList<>();
    private Round round;
    private boolean initialized = false;

    public enum Round {
        FIRST_BETTING, CHANGE_CARDS, SECOND_BETTING, COMPARING_CARDS, SET_OVER, GAME_OVER
    }

    public Game() {}

    public Game(int gameId, int gameAnte, int maxPlayers, int startingMoney, ArrayList<Player> gamePlayers) {
        this.id = gameId;
        this.ante = gameAnte;
        this.maxPlayersNumber = maxPlayers;
        this.players = gamePlayers;
        for(Player player : players) {
            player.setMoney(startingMoney);
            player.setCurrentGameId(id);
        }
    }

    public void setId(int gameId) {this.id = gameId;}

    public int getId() {
        return id;
    }

    public void setAnte(int gameAnte) {this.ante = gameAnte;}

    public int getAnte() {
        return ante;
    }

    public void setPot(int gamePot) {this.pot = gamePot;}

    public int getPot() {
        return pot;
    }

    public int getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }

    public void setMaxPlayersNumber(int maxPlayers) {this.maxPlayersNumber = maxPlayers;}

    public int getMaxPlayersNumber() {return this.maxPlayersNumber;}

    public void setStartingMoney(int money) {this.startingMoney = money;}

    public int getStartingMoney() {return startingMoney;}

    public Player getDealer() {
        return dealer;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public void playerFolds(Player p) {
        activePlayers.remove(p);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void takeAnteFromPlayer() {
        pot = 0;
        round = Round.FIRST_BETTING;
        for(Player p : players) {
            if(p.getMoney() != 0 ) {
                for(int i=0; i<5; i++) {
                    p.addCard(deck.dealCard());
                }
                int pMoney = p.getMoney();
                p.setMoney(Math.max(pMoney - ante, 0));
                p.setInPot(pMoney - p.getMoney());
                pot += p.getInPot();
                p.setBid(0);

            }
            else {
                p.setActive(false);
            }
        }
    }

    public String gameInfo() {
        String info = "";
        info += "Round=" + round + "|Pot=" + pot + ",";
        for(Player p : players) {
            info += "|Player" + p.getId() + "|Money=" + p.getMoney() + "|InGame=" + p.isActive();
            if(p.isActive()) {
                info += "|Action=" + p.getAction() + "|InPot=" + p.getInPot() + "|Bid=" + p.getBid();
            }
            if(dealer.getId()==p.getId()) {
                info += "|Dealer";
            }
            if(playerTurn.getId()==p.getId()) {
                info += "|Turn";
            }
            info += ",";
        }

        return info;
    }

    public Player getPlayerAtIndex(int i) {
        return players.get(i);
    }

    public ArrayList<Player> gamePlayers() {
        return  players;
    }

    public ArrayList<Player> activePlayers() {
        return  activePlayers;
    }

    public void addActivePlayer(Player p) {
        if (!activePlayers.contains(p)) {
            activePlayers.add(p);
        }
    }

    public void updatePot() {
        pot = 0;
        for(Player p : players) {
            pot += p.getInPot();
        }
    }

    public void giveMoneyToPlayers() {
        for(Player p : players) {
            p.setMoney(startingMoney);
        }
    }

    public void initializeGame() {
        if(!initialized) {
            setDealer(players.get(0));
            setPlayerTurn(players.get(1));
            giveMoneyToPlayers();
            takeAnteFromPlayer();
            for (Player p : players) {
                p.setActive(true);
                p.setAction(Player.Action.NONE);
                p.setHand();
            }
            maxBid = 0;
            initialized = true;
        }
    }

    public void nextPhase() {
        switch (round){
            case FIRST_BETTING:
                round = Round.CHANGE_CARDS;
                break;
            case CHANGE_CARDS:
                round = Round.SECOND_BETTING;
                break;
            case SECOND_BETTING:
                round = Round.COMPARING_CARDS;
                break;
        }
    }

    public void proceedBettingRound() {
        if(activePlayers.size()>1) {
            boolean areEqual = true;
            for(Player p : activePlayers) {
                if(p.getBid() != maxBid) {
                    areEqual = false;
                    break;
                }
            }
            if(areEqual) {
                nextPhase();
            }
            else {
                int index=0;
                for(int i=0; i<activePlayers.size(); i++) {
                    if(activePlayers.get(i).getId()==playerTurn.getId()) {
                        index = (i+1)%activePlayers.size();
                    }
                }
                playerTurn = activePlayers.get(index);
            }
        }
        else {
            round = Round.SET_OVER;
            activePlayers.get(0).updateMoney(pot);
        }
    }
}
