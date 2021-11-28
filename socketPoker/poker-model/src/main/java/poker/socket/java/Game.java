package poker.socket.java;

import java.util.ArrayList;
import java.util.Comparator;
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
    private ArrayList<Player> playersByHand = new ArrayList<>();
    private Round round;
    private boolean initialized = false;
    private Player potWinner = null;
    private Player winner = null;
    private boolean finished = false;
    private boolean setProceeded = false;
    private String allFolded = "00";
    private boolean roundProceeded = false;
    private int playersToProceedRound = 0;
    public boolean anteTaken = false;

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

    public String isAllFolded() {
        return allFolded;
    }

    public void setAllFolded(String allFolded) {
        this.allFolded = allFolded;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void takeAnteFromPlayer() {
        pot = 0;
        //round = Round.FIRST_BETTING;
        for(Player p : players) {
            if(p.getMoney() != 0 ) {
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
        anteTaken = true;
    }

    public void dealCardsToActivePlayers() {
        for(Player p : activePlayers) {
            for(int i=0; i<5; i++) {
                p.addCard(deck.dealCard());
            }
            p.setHand();
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

    public Player getPotWinner() {
        return potWinner;
    }

    public void setPotWinner(Player potWinner) {
        this.potWinner = potWinner;
    }

    public Player getWinner() {
        return potWinner;
    }

    public void setWinner(Player potWinner) {
        this.potWinner = potWinner;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isSetProceeded() {
        return setProceeded;
    }

    public void setSetProceeded(boolean setProceeded) {
        this.setProceeded = setProceeded;
    }

    public boolean isRoundProceeded() {
        return roundProceeded;
    }

    public void setRoundProceeded(boolean roundProceeded) {
        this.roundProceeded = roundProceeded;
    }

    public int getPlayersToProceedRound() {
        return playersToProceedRound;
    }

    public void setPlayersToProceedRound(int playersToProceedRound) {
        this.playersToProceedRound = playersToProceedRound;
    }

    public void updatePlayersToProceedRound(int playersToProceedRound) {
        this.playersToProceedRound += playersToProceedRound;
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
            //dealCardsToActivePlayers();
            for(Player p : players) {
                for(int i=0; i<5; i++) {
                    p.addCard(deck.dealCard());
                }
                p.setHand();
            }
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
        int biddingPlayersCount = 0;
        for(Player p : activePlayers) {
            if(p.isActive() && !p.getAction().equals(Player.Action.FOLD)) {
                biddingPlayersCount++;
            }
        }
        if(biddingPlayersCount>1) {
            boolean areEqual = false;
            int countOfTrue = 0;
            for(Player p : activePlayers) {
                if((p.getBid()==maxBid && !p.getAction().equals(Player.Action.NONE)) || (p.getInPot()!=0 && p.getMoney()==0)) {
                    countOfTrue++;
                }
            }
            if(countOfTrue==activePlayers.size()) {
                areEqual = true;
            }
            if(areEqual) {
                maxBid = 0;
                for(Player p : players) {
                    p.setBid(0);
                }
                nextPhase();
                resetTurnOnNewPhase();
            }
            else {
                int index=0;
                for(int i=0; i<activePlayers.size(); i++) {
                    if(activePlayers.get(i).getId()==playerTurn.getId() && !activePlayers.get((i+1)%activePlayers.size()).getAction().equals(Player.Action.FOLD)) {
                        index = (i+1)%activePlayers.size();
                    }
                }
                playerTurn = activePlayers.get(index);
            }
        }
        else {
            round = Round.SET_OVER;
            for(int i=0; i<activePlayers.size(); i++) {
                if(activePlayers.get(i).getAction().equals(Player.Action.BID)) {
                    activePlayers.get(i).updateMoney(pot);
                    allFolded = String.valueOf(activePlayers.get(i).getId());
                }
                //activePlayers.get(i).setAction(Player.Action.NONE);
                activePlayers.get(i).setInPot(0);
                activePlayers.get(i).setBid(0);
                for(int j=0; j<5; j++) {
                    Card activePlayerCard = activePlayers.get(i).getCardAtIndex(0);
                    deck.addCard(activePlayerCard);
                    activePlayers.get(i).removeCard(activePlayerCard);
                }
                //takeAnteFromPlayer();
                for(int n=0; n<5; n++) {
                    activePlayers.get(i).addCard(deck.dealCard());
                }
                activePlayers.get(i).setHand();
                }
            maxBid = 0;
            pot = 0;
            boolean newDealerFound = false;
            int index = dealer.getId() + 1;
            int dealerIndex;
            while(!newDealerFound) {
                if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                    dealer = activePlayers.get((index) % activePlayers.size());
                    dealerIndex = index;
                    newDealerFound = true;
                }
                index++;
            }
            boolean newTurnFound = false;
            while(!newTurnFound) {
                if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                    playerTurn = activePlayers.get((index) % activePlayers.size());
                    newTurnFound = true;
                }
                index++;
            }
            }
    }

//    public void proceedChangeRound() {
//
//    }

    public void resetTurnOnNewPhase() {
        Player cardDealer;
        int index = 0;
        boolean newTurnFound = false;
        for(int i=0; i<activePlayers.size(); i++) {
            if(activePlayers.get(i).getId()==dealer.getId()) {
                cardDealer = activePlayers.get(i);
                index = i;
            }
        }
        index++;
        while(!newTurnFound) {
            if(!activePlayers.get(index%activePlayers.size()).getAction().equals(Player.Action.FOLD)) {
                playerTurn = activePlayers.get(index%activePlayers.size());
                newTurnFound = true;
            }
            index++;
        }
    }

    static class SortPlayersByHand implements Comparator<Player> {
        @Override
        public int compare(Player o1, Player o2) {
            return -o1.getHand().compareTo(o2.getHand());
        }
    }

    public void comparePlayersCards() {
        playersByHand = activePlayers;
        playersByHand.sort(new SortPlayersByHand());
    }

    public void distributePot() {
        playersByHand.get(0).updateMoney(pot);
        setPot(0);
        for(Player p : players) {
            p.setInPot(0);
            p.setAction(Player.Action.NONE);
        }
        potWinner = playersByHand.get(0);
    }

    public void proceedAfterComparingCards() {
        int countOfActivePlayers = 0;
        activePlayers.clear();
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getMoney()==0  && players.get(i).isActive()) {
                players.get(i).setCurrentGameId(-1);
                players.get(i).setActive(false);
                for(int j=0; j<5; j++) {
                    Card inActivePlayerCard = players.get(i).getCardAtIndex(0);
                    deck.addCard(inActivePlayerCard);
                    players.get(i).removeCard(inActivePlayerCard);
                }
            }
            if(players.get(i).isActive()) {
                countOfActivePlayers++;
                activePlayers.add(players.get(i));
            }
        }
        if(activePlayers.size() > 1) {
            //new set
            //round = Round.FIRST_BETTING;
            boolean newDealerFound = false;
            int index = dealer.getId() + 1;
            while(!newDealerFound) {
                if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                    dealer = activePlayers.get((index) % activePlayers.size());
                    newDealerFound = true;
                }
                index++;
            }
            boolean newTurnFound = false;
            while(!newTurnFound) {
                if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                    playerTurn = activePlayers.get((index) % activePlayers.size());
                    newTurnFound = true;
                }
                index++;
            }
            for(int i=0; i<activePlayers.size(); i++) {
                for(int j=0; j<5; j++) {
                    Card activePlayerCard = activePlayers.get(i).getCardAtIndex(0);
                    deck.addCard(activePlayerCard);
                    activePlayers.get(i).removeCard(activePlayerCard);
                }
            }
            //new card deal
            maxBid = 0;
            //takeAnteFromPlayer();
            dealCardsToActivePlayers();
            for(int n=0 ;n<activePlayers.size(); n++) {
                activePlayers.get(n).setHand();
            }

        }
        else {
            //game over
            winner = activePlayers.get(0);
            finished = true;
            //round = Round.GAME_OVER;
        }
    }
}
